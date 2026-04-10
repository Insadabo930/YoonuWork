package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.CourStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class CourDTO {
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 150, message = "Le titre doit contenir entre 3 et 150 caractères")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, message = "La description doit contenir au moins 10 caractères")
    private String description;

    @NotBlank(message = "L'URL de la vidéo est obligatoire")
    @URL(message = "L'URL de la vidéo n'est pas valide")
    private String urlVideo;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être supérieure à 0")
    private Integer duree;

    @NotNull(message = "Le statut est obligatoire")
    private CourStatus courStatus;

    @NotNull(message = "L'id du chercheurEmploi est obligatoire")
    private Long formateurId;


    public CourDTO() {
    }

    public CourDTO(CourStatus courStatus, String description, Integer duree, Long formateurId, Long id, String titre, String urlVideo) {
        this.courStatus = courStatus;
        this.description = description;
        this.duree = duree;
        this.formateurId = formateurId;
        this.id = id;
        this.titre = titre;
        this.urlVideo = urlVideo;
    }

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public CourStatus getCourStatus() {
        return courStatus;
    }

    public void setCourStatus(CourStatus courStatus) {
        this.courStatus = courStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
