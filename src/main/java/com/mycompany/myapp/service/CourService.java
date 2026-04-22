package com.mycompany.myapp.service;


import com.mycompany.myapp.domain.Cour;
import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.domain.enumeration.CourStatus;
import com.mycompany.myapp.repository.CourRepository;
import com.mycompany.myapp.repository.UtilisateurRepository;
import com.mycompany.myapp.service.dto.CourCreationDTO;
import com.mycompany.myapp.service.dto.CourDTO;
import com.mycompany.myapp.service.mapper.CourMapper;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourService {

    private static final Logger log = LoggerFactory.getLogger(CourService.class);
    private final UtilisateurRepository utilisateurRepository;
    private final CourRepository courRepository;
    private final CourMapper courMapper;

    public CourService(CourMapper courMapper, UtilisateurRepository utilisateurRepository, CourRepository courRepository) {
        this.courMapper = courMapper;
        this.utilisateurRepository = utilisateurRepository;
        this.courRepository = courRepository;
    }

    private Utilisateur getAuthentifie() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtToken)) {
            throw new BadRequestAlertException("Authentification JWT requise", "cour", "jwtRequired");
        }

        String keycloakId = jwtToken.getToken().getSubject();

        return utilisateurRepository.findByKeycloakId(keycloakId)
            .orElseThrow(() -> new BadRequestAlertException(
                "Utilisateur non trouvé avec keycloakId: " + keycloakId, "cour", "userNotFound"
            ));    
    }
    private Cour getCourDuFormateur(Long courId, Utilisateur formateur) {
        Cour cour = courRepository.findById(courId)
            .orElseThrow(() -> new BadRequestAlertException(
                "Cours non trouvé", "cour", "notFound"
            ));

        if (!cour.getFormateur().getId().equals(formateur.getId())) {
            throw new BadRequestAlertException(
                "Vous n'êtes pas autorisé à modifier ce cours",
                "cour",
                "notOwner"
            );
        }

        return cour;
    }

    public CourDTO creerCourAvecKeycloakId(CourCreationDTO courDTO) {
        Utilisateur formateur = getAuthentifie();

        log.debug("Création de cours par formateur: {}", formateur.getId());

        Cour cour = courMapper.toEntity(courDTO);
        cour.setFormateur(formateur);
        cour.setCourStatus(
            cour.getCourStatus() != null ? cour.getCourStatus() : CourStatus.BROUILLON
        );

        return courMapper.toDto(courRepository.save(cour));
    }

    public CourDTO mettreAJourCour(Long courId, CourCreationDTO courDTO) {
        Utilisateur formateur = getAuthentifie();

        log.debug("Mise à jour du cours {} par formateur: {}", courId, formateur.getId());


        Cour courExistant = getCourDuFormateur(courId, formateur);


        courExistant.setTitre(courDTO.getTitre());
        courExistant.setDescription(courDTO.getDescription());
        courExistant.setUrlVideo(courDTO.getUrlVideo());
        courExistant.setDuree(courDTO.getDuree());
        courExistant.setCourStatus(
            courDTO.getCourStatus() != null ? courDTO.getCourStatus() : CourStatus.BROUILLON
        );

        return courMapper.toDto(courRepository.save(courExistant));
    }


    public CourDTO mettreAJourPartiellementCour(Long courId, CourCreationDTO courDTO) {
        Utilisateur formateur = getAuthentifie();

        log.debug("Mise à jour partielle du cours {} par formateur: {}", courId, formateur.getId());

        Cour courExistant = getCourDuFormateur(courId, formateur);


        if (courDTO.getTitre() != null) {
            courExistant.setTitre(courDTO.getTitre());
        }
        if (courDTO.getDescription() != null) {
            courExistant.setDescription(courDTO.getDescription());
        }
        if (courDTO.getUrlVideo() != null) {
            courExistant.setUrlVideo(courDTO.getUrlVideo());
        }
        if (courDTO.getDuree() != null) {
            courExistant.setDuree(courDTO.getDuree());
        }
        if (courDTO.getCourStatus() != null) {
            courExistant.setCourStatus(courDTO.getCourStatus());
        }

        return courMapper.toDto(courRepository.save(courExistant));
    }

    @Transactional(readOnly = true)
    public List<CourDTO> obtenirMesCours() {
        Utilisateur formateur = getAuthentifie();

        log.debug("Récupération des cours pour le formateur: {}", formateur.getId());

        return courRepository.findByFormateurId(formateur.getId())
            .stream()
            .map(courMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourDTO obtenirCourParId(Long courId) {
        Utilisateur formateur = getAuthentifie();

        log.debug("Lecture du cours {} par le formateur: {}", courId, formateur.getId());

        Cour cour = getCourDuFormateur(courId, formateur);

        return courMapper.toDto(cour);
    }



    public void supprimerCour(Long courId) {
        Utilisateur formateur = getAuthentifie();

        log.debug("Suppression du cours {} par formateur: {}", courId, formateur.getId());

        Cour cour = getCourDuFormateur(courId, formateur);

        courRepository.delete(cour);

        log.debug("Cours {} supprimé avec succès", courId);
    }

}

