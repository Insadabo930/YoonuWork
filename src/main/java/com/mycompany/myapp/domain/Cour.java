package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CourStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cours")
public class Cour implements Serializable {
    private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cour_sequence")
        @SequenceGenerator(name = "cour_sequence")
        private Long id;

        @Column(nullable = false, length = 150)
        private String titre;

        @Column(nullable = false, columnDefinition = "TEXT")
        private String description;

        @Column(name = "url_video", nullable = false)
        private String urlVideo;

        @Column(nullable = false)
        private Integer duree; // en secondes

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private CourStatus courStatus;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "formateur_id", nullable = false)
       @JsonIgnoreProperties(value = {"cours"}, allowSetters = true)
       private Utilisateur formateur;


        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }



    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public CourStatus getCourStatus() {
        return courStatus;
    }

    public void setCourStatus(CourStatus courStatus) {
        this.courStatus = courStatus;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public Utilisateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Utilisateur formateur) {
        this.formateur = formateur;
    }

    @Override
    public boolean equals(Object o){
            if(this==o){
                return true;
            }
            if(!(o instanceof Cour)){
                return false;
            }
            return getId() != null && getId().equals(((Cour)o).getId());
    }
    @Override
    public int hashCode(){
            return Objects.hashCode(getId());
        }

    @Override
    public String toString() {
        return "Cour{" +
            "courStatus=" + courStatus +
            ", id=" + id +
            ", titre='" + titre + '\'' +
            ", description='" + description + '\'' +
            ", urlVideo='" + urlVideo + '\'' +
            ", duree=" + duree +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

