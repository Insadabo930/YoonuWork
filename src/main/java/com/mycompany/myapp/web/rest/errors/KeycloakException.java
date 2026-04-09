package com.mycompany.myapp.web.rest.errors;

public class KeycloakException extends RuntimeException {
  public KeycloakException(String message) {
    super(message);
  }
}
