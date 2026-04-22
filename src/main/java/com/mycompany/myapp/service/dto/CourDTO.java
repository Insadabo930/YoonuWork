package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.CourStatus;

public class CourDTO {

    private Long id;

    private String titre;

    private String description;

    private String urlVideo;

    private Integer duree;

    private CourStatus courStatus;

    private Long formateurId;

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

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }
}
