package com.mycompany.myapp.service.dto;


import com.mycompany.myapp.domain.enumeration.TypePublication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PublicationCreationDTO {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100)
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, message = "La description doit faire au moins 10 caractères")
    private String description;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;

    @NotNull(message = "La durée est obligatoire")
    @Positive(message = "La durée doit être un nombre positif")
    private Integer dureeJours;

    @NotNull(message = "La rémunération est obligatoire")
    @Positive(message = "La rémunération doit être un nombre positif")
    private BigDecimal remuneration;

    @NotBlank(message = "Le type de contrat est obligatoire")
    private String typeContrat;

    @NotNull(message = "Le type de publication est obligatoire (MISSION ou OFFRE)")
    private TypePublication type;

    // ───────────── Getters / Setters ─────────────

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
}
