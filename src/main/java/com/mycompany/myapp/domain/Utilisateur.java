package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.domain.enumeration.UtilisateurRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utilisateur_sequence")
    @SequenceGenerator(name = "utilisateur_sequence")
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Email                                           // ← validation format email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",
        message = "Format invalide — utilisez E.164 : +221771234567")
    @Column(name = "telephone", length = 20, unique = true, nullable = false)
    private String telephone;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "keycloak_id", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String keycloakId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UtilisateurRole role;                    // ← rôle ajouté
    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"formateur"},allowSetters = true)
    private List<Cour> cours = new ArrayList<>();

    // ───────────── Getters / Setters ─────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getKeycloakId() { return keycloakId; }
    public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }

    public UtilisateurRole getRole() { return role; }
    public void setRole(UtilisateurRole role) { this.role = role; }

    public List<Cour> getCours() { return cours; }
    public void setCours(List<Cour> cours) { this.cours = cours; }

    // ───────────── Méthodes helper relation ─────────────

    public void addCour(Cour cour) {
        this.cours.add(cour);
        cour.setFormateur(this);
    }

    public void removeCour(Cour cour) {
        this.cours.remove(cour);
        cour.setFormateur(null);
    }

    // ───────────── equals / hashCode / toString ─────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", prenom='" + prenom + '\'' +
            ", nom='" + nom + '\'' +
            ", email='" + email + '\'' +
            ", telephone='" + telephone + '\'' +
            ", ville='" + ville + '\'' +
            ", role=" + role +
            '}';
    }
}
