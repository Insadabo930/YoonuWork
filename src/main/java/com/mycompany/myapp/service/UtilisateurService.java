package com.mycompany.myapp.service;

import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.repository.UtilisateurRepository;
import com.mycompany.myapp.service.dto.UtilisateurCreationDTO;
import com.mycompany.myapp.service.dto.UtilisateurDTO;
import com.mycompany.myapp.service.mapper.UtilisateurMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.KeycloakException;
import com.mycompany.myapp.web.rest.errors.KeycloakRollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;   // ← ajouté
import org.springframework.web.reactive.function.client.WebClient; // ← corrigé



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UtilisateurService {
    private static final Logger log = LoggerFactory.getLogger(UtilisateurService.class);


    private final UtilisateurRepository utilisateurRepository;
    private final WebClient webClient;              // ← corrigé
    private final ApplicationProperties applicationProperties;
    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurService(ApplicationProperties applicationProperties, UtilisateurRepository utilisateurRepository, WebClient webClient, UtilisateurMapper utilisateurMapper) {
        this.applicationProperties = applicationProperties;
        this.utilisateurRepository = utilisateurRepository;
        this.webClient = webClient;
        this.utilisateurMapper = utilisateurMapper;
    }

    // Todo: la method qui permet de generer le token admin
    private String authentification() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", applicationProperties.getClientId());
        map.add("client_secret", applicationProperties.getClientSecret());
        map.add("username", applicationProperties.getUserName());  // ← "admin"
        map.add("password", applicationProperties.getPassword());

        Map<String, Object> response = webClient.post()
            .uri(applicationProperties.getKcurl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(map))
            .retrieve()
            .onStatus(HttpStatusCode::isError, res ->
                res.bodyToMono(String.class).map(body ->
                    new KeycloakException("Echec authentifications keycloak HTTP " + res.statusCode()
                    )
                )
            )
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();
        if (response != null && response.containsKey("access_token")) {
            return (String) response.get("access_token");
        }
        throw new KeycloakException("Erreur lors de l'authentification sur keycloak !");
    }
    // Todo: creer un utilisateur avec l'api de keycloak
    public UtilisateurDTO creerUtilisateur(UtilisateurCreationDTO dto) {

        // 1. Validation mot de passe
        if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            throw new BadRequestAlertException("Le mot de passe est obligatoire","utilisateur","passwordMissing");
        }

        // 2. Comparer ensuite de façon null-safe
        if (!Objects.equals(dto.getMotDePasse(), dto.getConfirmeMotDePasse())) {
            throw new BadRequestAlertException("Les mots de passe ne correspondent pas","utilisateur","passwordInvalid");
        }

        // 2. Vérification doublons en BDD locale
        if (utilisateurRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestAlertException("Un utilisateur avec ce username existe dejà !","utilisateur","usernameInvalid");
        }
        if (utilisateurRepository.existsByTelephone(dto.getTelephone())) {
            throw new BadRequestAlertException("Un utilisateur avec ce numéro existe dejà !","utilisateur","telephoneAlreadyExist");
        }

        // 3. Provisionnement Keycloak
        String token = authentification();
        String keycloakUserId = provisionnerKeycloak(dto, token);

        // 4. Sauvegarde BDD avec rollback si échec
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(dto.getUsername());
            utilisateur.setPrenom(dto.getPrenom());
            utilisateur.setNom(dto.getNom());
            utilisateur.setTelephone(dto.getTelephone());
            utilisateur.setEmail(dto.getEmail());
            utilisateur.setRole(dto.getUtilisateurRole());
            utilisateur.setVille(dto.getVille());
            utilisateur.setKeycloakId(keycloakUserId);

            Utilisateur saved = utilisateurRepository.save(utilisateur);
            return utilisateurMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Échec BDD → rollback Keycloak pour : {}", keycloakUserId);

            webClient.delete()
                .uri(applicationProperties.getKcAdminUrl() + "/users/" + keycloakUserId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res ->
                    res.bodyToMono(String.class).map(error ->
                        new KeycloakRollbackException("Échec rollback Keycloak : " + error)
                    )
                )
                .toBodilessEntity()
                .block();

            log.info("Rollback effectué — keycloakId='{}' supprimé", keycloakUserId);
            throw new RuntimeException("Échec création en BDD — rollback Keycloak effectué", e);
        }


    }

    private String provisionnerKeycloak(UtilisateurCreationDTO dto, String token) {
        String baseUrl = applicationProperties.getKcAdminUrl();

        // --- Vérification existence dans Keycloak ---
        List<Map<String, Object>> existingUser = webClient.get()
            .uri(baseUrl + "/users?username=" + dto.getUsername() + "&exact=true")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .onStatus(HttpStatusCode::isError, res ->
                res.bodyToMono(String.class).map(error ->
                    new RuntimeException("Erreur vérification Keycloak : " + error)
                )
            )
            .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
            })
            .block();

        if (existingUser != null && !existingUser.isEmpty()) {
            throw new RuntimeException("Utilisateur déjà existant dans Keycloak : " + dto.getUsername());
        }

        // --- Construction du payload ---
        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", dto.getUsername());
        userRepresentation.put("firstName", dto.getPrenom());
        userRepresentation.put("lastName", dto.getNom());
        userRepresentation.put("email", dto.getEmail());
        userRepresentation.put("emailVerified", true);
        userRepresentation.put("enabled", true);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("telephone", List.of(dto.getTelephone()));
        attributes.put("ville", List.of(dto.getVille()));
        userRepresentation.put("attributes", attributes);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", dto.getMotDePasse());
        credential.put("temporary", false);
        userRepresentation.put("credentials", List.of(credential));


        //  --- Création dans Keycloak ---
        ResponseEntity<Void> response = webClient.post()
            .uri(baseUrl + "/users")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userRepresentation)
            .retrieve()
            .onStatus(HttpStatusCode::isError, res ->
                res.bodyToMono(String.class).map(error ->
                    new RuntimeException("Erreur création Keycloak : " + error)
                )
            )
            .toBodilessEntity()
            .block();

        String location = response != null ? response.getHeaders().getFirst("Location") : null;
        if (location == null) {
            throw new RuntimeException("Impossible de récupérer l'ID Keycloak après création");
        }

        String keycloakUserId = location.substring(location.lastIndexOf("/") + 1);
        log.info("Utilisateur créé dans Keycloak — keycloakId='{}'", keycloakUserId);


        // --- Récupération du rôle ---
        String roleName = dto.getUtilisateurRole().name();

        Map<String, Object> role = webClient.get()
            .uri(baseUrl + "/roles/" + roleName)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .onStatus(HttpStatusCode::isError, res ->
                res.bodyToMono(String.class).map(error ->
                    new RuntimeException("Rôle introuvable dans Keycloak : " + roleName)
                )
            )
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block();

        if (role == null) {
            throw new RuntimeException("Représentation du rôle null pour : " + roleName);
        }

        // --- Assignation du rôle ---
        webClient.post()
            .uri(baseUrl + "/users/" + keycloakUserId + "/role-mappings/realm")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(List.of(role))
            .retrieve()
            .onStatus(HttpStatusCode::isError, res ->
                res.bodyToMono(String.class).map(error ->
                    new RuntimeException("Échec assignation rôle : " + error)
                )
            )
            .toBodilessEntity()
            .block();

        log.info("Rôle '{}' assigné à '{}'", roleName, keycloakUserId);
        return keycloakUserId;
    }


//  public Map<String,Object> login(String username,String motDePasse){
//        log.debug("Tentative de connexion {}",username);
//        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
//            .orElseThrow(()->new RuntimeException("Utilisateur introuvable avec : "+username));
//        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
//        map.add("grant_type","password");
//        map.add("client_id", applicationProperties.getClientId());
//        map.add("client_secret", applicationProperties.getClientSecret());
//        map.add("username", username);
//        map.add("password", motDePasse);
//
//        Map<String,Object> response = webClient.post()
//            .uri(applicationProperties.getKcurl())
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .body(BodyInserters.fromFormData(map))
//            .retrieve()
//            .onStatus(HttpStatusCode::isError,res->
//                res.bodyToMono(String.class).map(error->
//                    new RuntimeException("Compte introuvable sur keycloak ou inacttif !")
//                )
//            )
//            .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
//            .block();
//        if(response == null || !response.containsKey("access_token")){
//            throw new  RuntimeException("Erreur Utilisateur introuvable !");
//        }
//
//        Map<String,Object> result = new HashMap<>();
//        result.put("access_token",response.get("access_token"));
//        result.put("refresh_token",response.get("refresh_token"));
//        result.put("token_type",response.get("token_type"));
//        result.put("utilisateur",Map.of(
//            "username",utilisateur.getUsername(),
//            "prenom",utilisateur.getPrenom(),
//            "nom",utilisateur.getNom(),
//            "email", utilisateur.getEmail(),
//            "ville",utilisateur.getVille(),
//            "telephone", utilisateur.getTelephone(),
//            "role", utilisateur.getRole()
//        ));
//      log.debug("Connexion réussie pour {}", username);
//        return result;
 // }

public Map<String, Object> login(String username, String motDePasse) {
    log.debug("Tentative de connexion {}", username);

    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec : " + username));

    // ← NOUVEAU : Convertir en DTO
    UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("client_id", applicationProperties.getClientId());
    map.add("client_secret", applicationProperties.getClientSecret());
    map.add("username", username);
    map.add("password", motDePasse);

    Map<String, Object> response = webClient.post()
        .uri(applicationProperties.getKcurl())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromFormData(map))
        .retrieve()
        .onStatus(HttpStatusCode::isError, res ->
            res.bodyToMono(String.class).map(error ->
                new RuntimeException("Compte introuvable sur keycloak ou inactif !")
            )
        )
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();

    if (response == null || !response.containsKey("access_token")) {
        throw new RuntimeException("Erreur Utilisateur introuvable !");
    }

    Map<String, Object> result = new HashMap<>();
    result.put("access_token", response.get("access_token"));
    result.put("refresh_token", response.get("refresh_token"));
    result.put("token_type", response.get("token_type"));

    // ← MODIFIÉ : Utiliser le DTO au lieu du Map
    result.put("utilisateur", utilisateurDTO);

    log.debug("Connexion réussie pour {}", username);
    return result;
}
}



