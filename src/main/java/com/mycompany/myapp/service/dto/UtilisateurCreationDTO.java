package com.mycompany.myapp.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.enumeration.UtilisateurRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UtilisateurCreationDTO {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String prenom;
    @NotBlank
    private String nom;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String motDePasse;
    @NotBlank
    private String confirmeMotDePasse;
    @NotBlank
    private String telephone;
    @NotBlank
    private String ville;
    @NotNull
    private UtilisateurRole utilisateurRole;


    public UtilisateurCreationDTO() {
    }

    public UtilisateurCreationDTO(String confirmeMotDePasse, String email, Long id, String motDePasse, String nom, String prenom, String telephone, String username, UtilisateurRole utilisateurRole, String ville) {
        this.confirmeMotDePasse = confirmeMotDePasse;
        this.email = email;
        this.id = id;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.username = username;
        this.utilisateurRole = utilisateurRole;
        this.ville = ville;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UtilisateurRole getUtilisateurRole() {
        return utilisateurRole;
    }

    public void setUtilisateurRole(UtilisateurRole utilisateurRole) {
        this.utilisateurRole = utilisateurRole;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getConfirmeMotDePasse() {
        return confirmeMotDePasse;
    }

    public void setConfirmeMotDePasse(String confirmeMotDePasse) {
        this.confirmeMotDePasse = confirmeMotDePasse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof UtilisateurCreationDTO)){
            return false;
        }
        UtilisateurCreationDTO utilisateurCreationDTO = (UtilisateurCreationDTO) o;
        if(this.id == null){
            return false;
        }
        return Objects.equals(this.id,utilisateurCreationDTO.id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UtilisateurCreationDTO{" +
            ", id=" + id +
            ", username='" + username + '\'' +
            ", prenom='" + prenom + '\'' +
            ", nom='" + nom + '\'' +
            ", email='" + email + '\'' +
            ", telephone='" + telephone + '\'' +
            ", ville='" + ville + '\'' +
            ", utilisateurRole=" + utilisateurRole +
            '}';
    }
}
