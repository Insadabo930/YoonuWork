package com.mycompany.myapp.web;



import com.mycompany.myapp.service.UtilisateurService;
import com.mycompany.myapp.service.dto.LoginDTO;
import com.mycompany.myapp.service.dto.UtilisateurCreationDTO;
import com.mycompany.myapp.service.dto.UtilisateurDTO;
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
    public ResponseEntity<UtilisateurDTO> inscrire(@Valid @RequestBody UtilisateurCreationDTO dto) {
        UtilisateurDTO reponse = utilisateurService.creerUtilisateur(dto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(reponse.getId())
            .toUri();

        return ResponseEntity.created(location).body(reponse);
    }

    @PostMapping("/login")   // ← POST, pas GET
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        Map<String, Object> response = utilisateurService.login(
            dto.getUsername(),
            dto.getMotDePasse()
        );
        return ResponseEntity.ok(response);
    }
}


