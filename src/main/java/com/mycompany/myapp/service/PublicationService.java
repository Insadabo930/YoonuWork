package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.domain.enumeration.UtilisateurRole;
import com.mycompany.myapp.repository.PublicationRepository;
import com.mycompany.myapp.repository.UtilisateurRepository;
import com.mycompany.myapp.service.dto.PublicationCreationDTO;
import com.mycompany.myapp.service.dto.PublicationDTO;
import com.mycompany.myapp.service.mapper.PublicationMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublicationService {
    private static final Logger log = LoggerFactory.getLogger(PublicationService.class);

    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private final UtilisateurRepository utilisateurRepository;

    public PublicationService(PublicationMapper publicationMapper,
                              PublicationRepository publicationRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.publicationMapper = publicationMapper;
        this.publicationRepository = publicationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    private Utilisateur getAuthentifie() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtToken)) {
            throw new BadRequestAlertException("Authentification JWT requise", "publication", "jwtRequired");
        }
        String keycloakId = jwtToken.getToken().getSubject();
        return utilisateurRepository.findByKeycloakId(keycloakId)
            .orElseThrow(() -> new BadRequestAlertException(
                "Utilisateur non trouvé avec keycloakId: " + keycloakId, "publication", "userNotFound"
            ));
    }

    // ===== MÉTHODE EXTRAITE — récupère la publication et vérifie la propriété =====
    private Publication getPublicationAvecVerification(Long id, Utilisateur authentifie) {
        Publication publication = publicationRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException(
                "Publication non trouvée avec id : " + id, "publication", "notFound"
            ));

        if (!publication.getEmployeur().getId().equals(authentifie.getId())) {
            throw new BadRequestAlertException(
                "Vous n'êtes pas autorisé à effectuer cette action sur cette publication",
                "publication", "forbidden"
            );
        }

        return publication;
    }


    public PublicationDTO creerPublication(PublicationCreationDTO dto) {
        Utilisateur employeur = getAuthentifie();
        log.debug("Création publication par utilisateur: {}", employeur.getId());

        if (!UtilisateurRole.ROLE_EMPLOYEUR.equals(employeur.getRole())) {
            throw new BadRequestAlertException(
                "Seul un employeur peut créer une publication",
                "publication", "roleIncompatible"
            );
        }

        Publication publication = publicationMapper.toEntity(dto);
        publication.setEmployeur(employeur);
        Publication saved = publicationRepository.save(publication);
        log.info("Publication '{}' créée avec succès (id={})", saved.getTitre(), saved.getId());
        return publicationMapper.toDto(saved);
    }


    public PublicationDTO update(Long id, PublicationDTO dto) {
        Utilisateur authentifie = getAuthentifie();
        Publication publication = getPublicationAvecVerification(id, authentifie);

        publicationMapper.updateEntityFromDto(dto, publication);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }


    public PublicationDTO partialUpdate(Long id, PublicationDTO dto) {
        Utilisateur authentifie = getAuthentifie();
        Publication publication = getPublicationAvecVerification(id, authentifie);

        publicationMapper.partialUpdateEntityFromDto(dto, publication);
        return publicationMapper.toDto(publicationRepository.save(publication));
    }


    public List<PublicationDTO> getAll() {
        return publicationRepository.findAll()
            .stream()
            .map(publicationMapper::toDto)
            .toList();
    }


    public PublicationDTO getById(Long id) {
        return publicationRepository.findById(id)
            .map(publicationMapper::toDto)
            .orElseThrow(() -> new BadRequestAlertException(
                "Publication non trouvée avec id : " + id, "publication", "notFound"
            ));
    }


    public void delete(Long id) {
        Utilisateur authentifie = getAuthentifie();
        Publication publication = getPublicationAvecVerification(id, authentifie);

        publicationRepository.deleteById(publication.getId());
        log.info("Publication id={} supprimée par utilisateur id={}", id, authentifie.getId());
    }
}
