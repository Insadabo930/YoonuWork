package com.mycompany.myapp.web.rest.errors;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 409 — Conflit (username ou téléphone déjà utilisé)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "status",    409,
            "erreur",    ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }

    // 502 — Erreur communication Keycloak
    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloak(KeycloakException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
            "status",    502,
            "erreur",    ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }

    // 500 — Échec BDD + rollback Keycloak
    @ExceptionHandler(KeycloakRollbackException.class)
    public ResponseEntity<Map<String, Object>> handleRollback(KeycloakRollbackException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "status",    500,
            "erreur",    ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }

    // 400 — Validation échouée (mots de passe différents, @Valid)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "status",    400,
            "erreur",    ex.getMessage(),
            "timestamp", LocalDateTime.now()
        ));
    }
}
