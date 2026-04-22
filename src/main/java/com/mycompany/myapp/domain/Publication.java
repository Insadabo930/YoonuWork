package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TypePublication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Publication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_sequence")
    @SequenceGenerator(name = "publication_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Size(min = 10)
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "localisation", nullable = false)
    private String localisation;

    @NotNull
    @Positive(message = "La durée doit être positive")
    @Column(name = "duree_jours", nullable = false)
    private Integer dureeJours;

    @NotNull
    @Positive(message = "La rémunération doit être positive")
    @Column(name = "remuneration", nullable = false, precision = 21, scale = 2)
    private BigDecimal remuneration;

    @NotNull
    @Column(name = "type_contrat", nullable = false)
    private String typeContrat;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypePublication type;

    @ManyToOne
    @JoinColumn(name = "employeur_id")
    @JsonIgnoreProperties(value = {"publications", "cours"}, allowSetters = true)
    private Utilisateur employeur;

    // ───────────── Getters / Setters ─────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Integer getDureeJours() { return dureeJours; }
    public void setDureeJours(Integer dureeJours) { this.dureeJours = dureeJours; }

    public BigDecimal getRemuneration() { return remuneration; }
    public void setRemuneration(BigDecimal remuneration) { this.remuneration = remuneration; }

    public String getTypeContrat() { return typeContrat; }
    public void setTypeContrat(String typeContrat) { this.typeContrat = typeContrat; }

    public TypePublication getType() { return type; }
    public void setType(TypePublication type) { this.type = type; }

    public Utilisateur getEmployeur() { return employeur; }
    public void setEmployeur(Utilisateur employeur) { this.employeur = employeur; }

    // ───────────── equals / hashCode / toString ─────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        return getId() != null && getId().equals(((Publication) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Publication{" +
            "id=" + id +
            ", titre='" + titre + '\'' +
            ", type=" + type +
            ", localisation='" + localisation + '\'' +
            ", dureeJours=" + dureeJours +
            ", remuneration=" + remuneration +
            ", typeContrat='" + typeContrat + '\'' +
            '}';
    }
}
