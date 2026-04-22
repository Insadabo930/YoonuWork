package com.mycompany.myapp.service.dto;



import com.mycompany.myapp.domain.enumeration.TypePublication;
import java.math.BigDecimal;

public class PublicationDTO {

    private Long id;
    private String titre;
    private String description;
    private String localisation;
    private Integer dureeJours;
    private BigDecimal remuneration;
    private String typeContrat;
    private TypePublication type;

    // Infos de l'employeur (pas l'objet entier pour éviter les données sensibles)
    private Long employeurId;
    private String employeurUsername;
    private String employeurVille;

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

    public Long getEmployeurId() { return employeurId; }
    public void setEmployeurId(Long employeurId) { this.employeurId = employeurId; }

    public String getEmployeurUsername() { return employeurUsername; }
    public void setEmployeurUsername(String employeurUsername) { this.employeurUsername = employeurUsername; }

    public String getEmployeurVille() { return employeurVille; }
    public void setEmployeurVille(String employeurVille) { this.employeurVille = employeurVille; }
}
