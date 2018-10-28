package model;

import com.sun.deploy.security.ValidationState;

import java.io.Serializable;
import java.util.ArrayList;

public class Contrainte implements Serializable {


    public enum CODE {REDONDANCE_EQUIPIER, REDONDANCE_ADVERSAIRE, EQUIPE_MIXTE, ECART_MAXI, JOUEUR_ATTENTE;};


    public enum TYPE_TOURNOI_CONTRAINTE {SIMPLE, DOUBLE, BOTH;};
    private CODE code;

    private TYPE_TOURNOI_CONTRAINTE typeTournoi;
    private String nom;
    private String description;
    private boolean actif;
    private String urlPhoto;
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Contrainte(String nom, String description, boolean actif, CODE code, TYPE_TOURNOI_CONTRAINTE typeTournoi, String url) {
        this.nom = nom;
        this.description = description;
        this.actif = actif;
        this.code = code;
        this.typeTournoi = typeTournoi;
        this.urlPhoto = url;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public CODE getCode() {
        return code;
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




}
