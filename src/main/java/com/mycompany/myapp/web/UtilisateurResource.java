package com.mycompany.myapp.web;


import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.service.UtilisateurService;
import com.mycompany.myapp.service.dto.LoginDTO;
import com.mycompany.myapp.service.dto.UtilisateurCreationDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UtilisateurResource {


    private static final Logger log = LoggerFactory.getLogger(UtilisateurResource.class);
    private final UtilisateurService utilisateurService;

    public UtilisateurResource(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<Utilisateur> inscrire(
        @Valid @RequestBody UtilisateurCreationDTO dto) {

        log.info("Requête REST pour créer un utilisateur : {}", dto.getUsername());

        Utilisateur reponse = utilisateurService.creerUtilisateur(dto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(reponse.getId())
            .toUri();

        log.info("Utilisateur créé avec succès : id={}", reponse.getId());

        return ResponseEntity.created(location).body(reponse);
    }

        @GetMapping("/login")
        public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO dto) {
            Map<String, Object> response = utilisateurService.login(
                dto.getUsername(),
                dto.getMotDePasse()
            );
            return ResponseEntity.ok(response);
        }
    }


