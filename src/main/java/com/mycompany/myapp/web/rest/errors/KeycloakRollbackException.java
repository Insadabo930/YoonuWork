package com.mycompany.myapp.web.rest.errors;

public class KeycloakRollbackException extends RuntimeException {
    public KeycloakRollbackException(String message) {
        super(message);
    }
}
