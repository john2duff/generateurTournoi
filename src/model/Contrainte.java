package model;

import com.sun.deploy.security.ValidationState;

import java.io.Serializable;
import java.util.ArrayList;

public class Contrainte implements Serializable {

    private Tournoi.TypeTournoi typeTournoi;
    private String nom;
    private String description;
    private boolean actif;
    private boolean visible;
    private String urlPhoto;
    public boolean isActif() {
        return actif;
    }
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Contrainte(String nom, String description, boolean actif, boolean visible, Tournoi.TypeTournoi typeTournoi, String url) {
        this.nom = nom;
        this.description = description;
        this.actif = actif;
        this.typeTournoi = typeTournoi;
        this.urlPhoto = url;
        this.visible = visible;
    }

    public Tournoi.TypeTournoi getTypeTournoi() {
        return typeTournoi;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
