package com.mycompany.myapp.web;


import com.mycompany.myapp.service.PublicationService;
import com.mycompany.myapp.service.dto.PublicationCreationDTO;
import com.mycompany.myapp.service.dto.PublicationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publications")
public class PublicationResource {

    private static final Logger log = LoggerFactory.getLogger(PublicationResource.class);

    private final PublicationService publicationService;

    public PublicationResource(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    // ===== POST — création =====
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEUR')")
    public ResponseEntity<PublicationDTO> creerPublication(@RequestBody PublicationCreationDTO dto) {
        log.debug("REST request to create Publication : {}", dto);
        PublicationDTO result = publicationService.creerPublication(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ===== PUT — remplacement complet =====
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEUR')")
    public ResponseEntity<PublicationDTO> updatePublication(
        @PathVariable Long id,
        @RequestBody PublicationDTO dto) {
        log.debug("REST request to update Publication : {}", id);
        PublicationDTO result = publicationService.update(id, dto);
        return ResponseEntity.ok(result);
    }

    // ===== PATCH — mise à jour partielle =====
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEUR')")
    public ResponseEntity<PublicationDTO> partialUpdatePublication(
        @PathVariable Long id,
        @RequestBody PublicationDTO dto) {
        log.debug("REST request to partial update Publication : {}", id);
        PublicationDTO result = publicationService.partialUpdate(id, dto);
        return ResponseEntity.ok(result);
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEUR')")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);
        publicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
