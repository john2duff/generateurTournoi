package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Joueur implements Serializable {

    public enum Sexe {HOMME, FEMME;};

    private String photoUrl;
    private String prenom;
    private String nom;
    private Niveau niveau;
    private Sexe sexe;
    private Boolean actif;

    private ArrayList<Joueur> listAdversaires;
    private ArrayList<Joueur> listEquipier;

    public Joueur(String prenom, String nom, Niveau niveau, Sexe sexe, boolean actif) {
        this.photoUrl = null;
        this.prenom = prenom;
        this.nom = nom;
        this.niveau = niveau;
        this.sexe = sexe;
        this.actif = actif;
        this.listEquipier = new ArrayList<Joueur>();
        this.listAdversaires = new ArrayList<Joueur>();
    }

    public boolean isActif() {
        return actif;
    }
    public String getPhoto() {
        return photoUrl;
    }

    public void setPhoto(String absolutePath) {
        photoUrl = absolutePath;
    }

    public Integer getPointsHandicap(){
        return Niveau.getNiveau(niveau.getNomNiveau()).getPoints();
    }

    @Override
    public String toString(){
        return prenom;
    }

    public boolean aDejaJoueContre (Joueur joueur){
        return listAdversaires.contains(joueur);
    }
    public boolean aDejaJoueAvec (Joueur joueur){
        return listEquipier.contains(joueur);
    }

    @Override
    public boolean equals(Object o){
        if (o != null){
            return (this.prenom.equals(((Joueur)o).prenom) && this.nom.equals(((Joueur)o).nom));
        }else{
            return false;
        }
    }

    public String getSexeToString() {
        switch(sexe) {
            case HOMME:
                return "Homme";
            case FEMME:
                return "Femme";
            default: return null;
        }
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void addEquipier(Joueur joueur) {
        if (joueur != this && !listEquipier.contains(joueur)){
            listEquipier.add(joueur);
        }
    }

    public void addAdversaire(Joueur joueur) {
        if (joueur != this && !listAdversaires.contains(joueur)){
            listAdversaires.add(joueur);
        }
    }

}
