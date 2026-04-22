package com.mycompany.myapp.web;


import com.mycompany.myapp.service.CourService;
import com.mycompany.myapp.service.dto.CourCreationDTO;
import com.mycompany.myapp.service.dto.CourDTO;
import java.util.List;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/cours")
public class CourResource {

    private static final Logger log = LoggerFactory.getLogger(CourResource.class);
    private static final String ENTITY_NAME = "cour";

    private final CourService courService;

    public CourResource(CourService courService) {
        this.courService = courService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<CourDTO> createCour(@Valid @RequestBody CourCreationDTO courCreationDTO) {
        log.debug("REST request to save Cour : {}", courCreationDTO);

        CourDTO result = courService.creerCourAvecKeycloakId(courCreationDTO);

        return ResponseEntity
            .created(URI.create("/api/cours/" + result.getId()))
            .body(result);
    }
    @GetMapping
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<List<CourDTO>> getMesCours() {
        return ResponseEntity.ok(courService.obtenirMesCours());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<CourDTO> getCour(@PathVariable Long id) {
        return ResponseEntity.ok(courService.obtenirCourParId(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<CourDTO> updateCour(@PathVariable Long id, @Valid @RequestBody CourCreationDTO courDTO) {
        return ResponseEntity.ok(courService.mettreAJourCour(id, courDTO));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<CourDTO> partialUpdateCour(@PathVariable Long id, @RequestBody CourCreationDTO courDTO) {
        return ResponseEntity.ok(courService.mettreAJourPartiellementCour(id, courDTO));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATEUR')")
    public ResponseEntity<Void> deleteCour(@PathVariable Long id) {
        courService.supprimerCour(id);
        return ResponseEntity.noContent().build();
    }
}
