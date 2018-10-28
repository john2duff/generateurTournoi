package model;

import com.sun.deploy.security.ValidationState;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Tournoi implements Serializable {

    private String nomTournoi;

    public void clotureTour() {
        if (currentTour < listTours.size()){
            currentTour++;
        }else{
            System.out.println("tournoi fini");
        }
    }

    public void changeScore(Integer numeroTour, Integer numeroMatch, Integer newValue) {
        listTours.get(numeroTour).getListMatchs().get(numeroMatch).setScoreEquipeA(Integer.parseInt(String.valueOf(newValue)));
    }


    public enum TypeTournoi {SIMPLE, DOUBLE;}


    private TypeTournoi typeTournoi;

    private Integer nombreTour;
    private Integer nombreTerrain;

    private Integer ecartMaxi;

    private Integer currentTour;
    private ArrayList<Joueur> listJoueurs;
    private ArrayList<Joueur> listJoueursAttribues;
    private ArrayList<Joueur> listJoueursAttente;
    private ArrayList<Contrainte> listContraintes; //priorisées
    private ArrayList<Tour> listTours;
    private ArrayList<Joueur> sac;
    private ArrayList<Niveau> listNiveaux;
    private Integer pointVictoire;
    private Integer pointDefaite;
    private Integer pointDefaitePlus;

    public Tournoi() {}

    public Tournoi(String nomTournoi, TypeTournoi type, Integer nombreTour, Integer nombreTerrain) {
        this.nomTournoi = nomTournoi;
        this.typeTournoi = type;
        this.nombreTour = nombreTour;
        this.nombreTerrain = nombreTerrain;
        this.listJoueurs = new ArrayList<Joueur>();
        this.listTours = new ArrayList<Tour>();
        this.currentTour = null;
        this.listNiveaux = Niveau.getNiveaux();
        this.pointVictoire = 5;
        this.pointDefaitePlus = 2;
        this.pointDefaite = 1;
        this.ecartMaxi = 10;
        loadContraintes();
    }


    public void loadContraintes(){
        listContraintes = new ArrayList<>();
        listContraintes.add(new Contrainte("Attente joueur", "On évite que les joueurs reste sur la touche pendant plusieurs tours.",
                true, Contrainte.TYPE_TOURNOI_CONTRAINTE.BOTH, "/img/contrainte-attente.png"));
        listContraintes.add(new Contrainte("Redondance équipier", "Le tirage au sort des équipe évite que l'on rejoue plusieurs fois avec le même équipier dans le cas d'un match en double.",
                true, Contrainte.TYPE_TOURNOI_CONTRAINTE.DOUBLE, "/img/contrainte-equipier.png"));
        listContraintes.add(new Contrainte("Redondance adversaire", "Le tirage au sort des équipe évite que l'on rejoue plusieurs fois contre le même adversaire.",
                true, Contrainte.TYPE_TOURNOI_CONTRAINTE.BOTH, "/img/contrainte-adversaire.png"));
        listContraintes.add(new Contrainte("Equipe mixte", "On ne permet pas qu'une équipe mixte puisse jouer avec une équipe non mixte.",
                true, Contrainte.TYPE_TOURNOI_CONTRAINTE.DOUBLE, "/img/contrainte-mixte.png"));
        listContraintes.add(new Contrainte("Ecart maxi", "On ne permet pas un écart de points de plus de X points entre les deux équipes.",
                true, Contrainte.TYPE_TOURNOI_CONTRAINTE.BOTH, "/img/scoreboard.png"));
    }

    public Contrainte getContrainte(String nomContrainte){
        for (int i = 0; i < listContraintes.size(); i++){
            if (listContraintes.get(i).getNom().equals(nomContrainte))
                return listContraintes.get(i);
        }
        return null;
    }

    public Integer getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour() {
        currentTour = null;
    }

    public void enregistreSelection(Integer ind, boolean actif) {
        listJoueurs.get(ind).setActif(actif);
    }
    public ArrayList<Tour> getListTours() {
        return listTours;
    }

    public Integer getEcartMaxi() {
        return ecartMaxi;
    }

    public void setEcartMaxi(Integer ecartMaxi) {
        this.ecartMaxi = ecartMaxi;
    }

    public void monterContrainte(String id) {
        Integer ind = Integer.valueOf(id);
        ArrayList<Contrainte> newListContrainte = new ArrayList<>(0);
        for (Integer i = 0; i < listContraintes.size(); i++){
            if (ind == i+1){
                newListContrainte.add(listContraintes.get(i+1));
                newListContrainte.add(listContraintes.get(i));
                i++;
            }else{
                newListContrainte.add(listContraintes.get(i));
            }
        }
        listContraintes = newListContrainte;
    }

    public void descendreContrainte(String id) {
        Integer ind = Integer.valueOf(id);
        ArrayList<Contrainte> newListContrainte = new ArrayList<>(0);
        for (Integer i = listContraintes.size()-1; i >= 0; i--){
            if (ind == i-1){
                newListContrainte.add(0, listContraintes.get(i-1));
                newListContrainte.add(0, listContraintes.get(i));
                i--;
            }else{
                newListContrainte.add(0, listContraintes.get(i));
            }
        }
        listContraintes = newListContrainte;
    }
    public void setListContraintes(ArrayList<Contrainte> contraintes) {
        this.listContraintes = contraintes;
    }

    public ArrayList<Contrainte> getListContraintes() {
        return listContraintes;
    }

    public void setListNiveau(ArrayList<Niveau> listNiveau) {
        this.listNiveaux = listNiveau;
    }

    public String getVictoireToString() {
        return pointVictoire.toString();
    }

    public Integer getVictoire() {
        return pointVictoire;
    }
    public void setVictoire(Integer p) {
        this.pointVictoire = p;
    }
    public String getDefaiteToString() {
        return pointDefaite.toString();
    }

    public Integer getDefaite() {
        return pointDefaite;
    }
    public void setDefaite(Integer p) {
        this.pointDefaite = p;
    }
    public String getDefaitePlusToString() {
        return pointDefaitePlus.toString();
    }

    public Integer getDefaitePlus() {
        return pointDefaitePlus;
    }
    public void setDefaitePlus(Integer p) {
        this.pointDefaitePlus = p;
    }

    public Integer getNombreTour() {
        return nombreTour;
    }

    public Integer getNombreTerrain() {
        return nombreTerrain;
    }

    public String getNombreTerrainToString() {
        return nombreTerrain.toString();
    }

    public String getNombreTourToString() {
        return nombreTour.toString();
    }

    public String getTypeTournoiToString() {
        if (typeTournoi == TypeTournoi.SIMPLE){
            return "Simple";
        }else{
            return "Double";
        }
    }

    public TypeTournoi getTypeTournoi(){
        return typeTournoi;
    }

    public void setTypeTournoi(TypeTournoi typeTournoi) {
        this.typeTournoi = typeTournoi;
    }

    public void setNombreTour(Integer nombreTour) {
        this.nombreTour = nombreTour;
    }

    public void setNombreTerrain(Integer nombreTerrain) {
        this.nombreTerrain = nombreTerrain;
    }

    public Joueur getJoueur(Integer ind) {
        return listJoueurs.get(ind);
    }

    public void updatePhoto(Integer id, String path) {
        listJoueurs.get(id).setPhoto(path);
    }


    public void supprimerTout() {
        listJoueurs.clear();
    }

    public void setNomTournoi(String nom) {
        nomTournoi = nom;
    }

    public void enregistreListJoueur(List<Joueur> listJoueurs) {
        for (int i = 0; i < listJoueurs.size(); i++){
            this.listJoueurs.get(i).setPrenom(listJoueurs.get(i).getPrenom());
            this.listJoueurs.get(i).setNom(listJoueurs.get(i).getNom());
            this.listJoueurs.get(i).setNiveau(listJoueurs.get(i).getNiveau());
            this.listJoueurs.get(i).setSexe(listJoueurs.get(i).getSexe());
        }
    }

    public void enregistreConfig(Tournoi tournoi) {
        //contexte
        this.typeTournoi = tournoi.typeTournoi;
        this.nombreTerrain = tournoi.nombreTerrain;
        this.nombreTour = tournoi.nombreTour;
        //points
        for (int i = 0; i < tournoi.getListNiveau().size(); i++){
            Niveau.getNiveaux().get(i).setPoints(tournoi.getListNiveau().get(i).getPoints());
        }
        this.pointVictoire = tournoi.getVictoire();
        this.pointDefaite = tournoi.getDefaite();
        this.pointDefaitePlus = tournoi.getDefaitePlus();
        //règles
        this.listContraintes = tournoi.listContraintes;
        this.ecartMaxi = tournoi.ecartMaxi;

    }

    private ArrayList<Niveau> getListNiveau() {
        return this.listNiveaux;
    }

    @Override
    public String toString(){
        String retour = "";
        String tab = "\t";
        for (int i = 0; i < listTours.size() ; i++){
            retour += "Tour " + (i+1) + " \n";
            ArrayList<Match> matchs = listTours.get(i).getListMatchs();
            for (int j = 0; j < matchs.size() ; j++){
                retour += tab + tab + "Match " + (j+1) + " : ";
                ArrayList<Joueur> joueurs;
                //equipe A
                joueurs = matchs.get(j).getEquipeA();
                for (int k = 0; k < joueurs.size() ; k++){
                    retour += joueurs.get(k);
                    if (typeTournoi == TypeTournoi.DOUBLE && k == 0) retour += " - ";
                }
                retour += " / ";
                //equipe B
                joueurs = matchs.get(j).getEquipeB();
                for (int k = 0; k < joueurs.size() ; k++){
                    retour += joueurs.get(k);
                    if (typeTournoi == TypeTournoi.DOUBLE && k == 0) retour += " - ";
                }
                if (matchs.get(j).redondanceAdversaire || matchs.get(j).redondanceAdversaire){
                    retour += tab + tab;
                    if (matchs.get(j).redondanceAdversaire) retour += " < Adversaire > ";
                    if (matchs.get(j).redondanceEquipier) retour += " < Equipier > ";
                }
                retour += "\n";
            }
            if (listTours.get(i).getJoueurRestant().size() > 0){
                retour += tab + tab + tab + "Joueurs restant : ";
                for (int l = 0; l < listTours.get(i).getJoueurRestant().size(); l++){
                    retour += listTours.get(i).getJoueurRestant().get(l).getPrenom() + " " + listTours.get(i).getJoueurRestant().get(l).getNom();
                }
                retour += "\n";
            }
        }
        return retour;
    }

    public ArrayList<Joueur> getListJoueurs() {
        return listJoueurs;
    }

    public boolean  ajouteJoueur(Joueur j){
        listJoueurs.add(j);
        if (!listJoueurs.contains(j)){
            return true;
        }
        return false;
    }

    public void supprimerJoueur(Joueur j) {
        listJoueurs.remove(j);
    }

    public String getNomTournoi() {
        return nomTournoi;
    }

    public void ajouteContrainte(Contrainte c){
        if (!listContraintes.contains(c)){
            listContraintes.add(c);
        }
    }

    public void ajouteTour(Tour t){
        if (!listTours.contains(t)){
            listTours.add(t);
        }
    }

    public boolean generer(){
        Joueur joueurTireAuSort;
        //si le tournoi n'a pas commencé
        if (currentTour == null){
            currentTour = 0;
        }

        for (int i = currentTour; i < listTours.size(); i++){
            listTours.remove(i);
            i--;
        }

        listJoueursAttente = new ArrayList<>();

        if (getNbreJoueurSuffisant()){
            for (int i = currentTour; i < nombreTour ; i++){
                Tour tour = new Tour(i);
                listJoueursAttribues = new ArrayList<Joueur>();
                mettreJoueurDansSac();
                Integer compt = 0;
                do {
                    Match match = new Match(compt);
                    //equipe A joueur 1
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_A);
                        if (joueurTireAuSort != null){
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_A);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                    //equipe B joueur 1
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_B);
                        if (joueurTireAuSort != null) {
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_B);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }

                    if (typeTournoi == TypeTournoi.DOUBLE){
                        //equipe A joueur 2
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_A);
                        if (joueurTireAuSort != null){
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_A);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                        //equipe B joueur 2
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_B);
                        if (joueurTireAuSort != null) {
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_B);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                    }

                    if (match.getEquipeA().size() > 0)
                        tour.ajouteMatch(match);
                    mettreJoueurDansSac();
                    compt++;
                } while (terrainRestant(tour) && !sacVide() && getNbreJoueurSuffisantDansSac());
                tour.setJoueurRestant(sac);
                for (int m = 0; m < sac.size(); m++){
                    listJoueursAttente.add(sac.get(m));
                }
                listTours.add(tour);
            }
            return true;
        }else{
            return false;
        }

    }

    private boolean getNbreJoueurSuffisantDansSac() {
        return (typeTournoi == TypeTournoi.SIMPLE) ? sac.size() >= 2 : sac.size() >= 4;
    }

    private boolean getNbreJoueurSuffisant() {
        return (typeTournoi == TypeTournoi.SIMPLE) ? getNbreJoueurSelection() >= 2 : getNbreJoueurSelection() >= 4;
    }

    private Integer getNbreJoueurSelection(){
        Integer compt = 0;
        for (int i = 0; i < listJoueurs.size(); i++){
            if (listJoueurs.get(i).isActif()) compt++;
        }
        return compt;
    }

    private Integer getLastContrainte() {
        return listContraintes.size()-1;
    }

    private boolean terrainRestant(Tour tour) {
        return tour.getListMatchs().size() < nombreTerrain;
    }

    public Joueur tireAuSort(Match match, Integer contrainteIgnore, Match.Equipe equipe){
        int alea;
        mettreJoueurDansSac();
        while (!sacVide()) {
            alea = (int) (Math.random() * sac.size());
            Joueur joueurAlea = sac.get(alea);
            if (contrainteBloquante(match, joueurAlea, contrainteIgnore, equipe)){
                retireJoueurDuSac(joueurAlea);
            }else{
                return joueurAlea;
            }
        }
        if (contrainteIgnore == 0 ){
            mettreJoueurDansSac();
            if (sac.size() == 0){
                return null;
            }else{
                alea = (int) (Math.random() * sac.size());
                return sac.get(alea);
            }
        }else{
            contrainteIgnore --;
            return tireAuSort(match, contrainteIgnore, equipe);
        }
    }

    private boolean contrainteBloquante(Match match, Joueur joueurAlea, Integer contrainteIgnore, Match.Equipe equipe) {
        for (int i = contrainteIgnore; i >= 0 ; i--){
            if (verifieContrainte(match, joueurAlea, listContraintes.get(i), equipe)){
                return true;
            }
        }
        return false;
    }

    //doit retourner false pour ne pas avoir d'alerte
    private boolean verifieContrainte(Match match, Joueur joueurAlea, Contrainte contrainte, Match.Equipe equipe) {
        switch(contrainte.getNom()){
            case "Redondance adversaire":
                return match.redondanceAdversaire(joueurAlea, equipe);
            case "Redondance équipier":
                return match.redondanceEquipier(joueurAlea, equipe);
            case "Equipe mixte":
                return match.equipeMixte(joueurAlea);
            case "Ecart maxi":
                //TODO
                return false;
            case "Attente joueur":
                return !listJoueursAttente.contains(joueurAlea);
            default:
                System.out.println("Contrainte inconnu");
                return false;
        }
    }

    public boolean sacVide(){
        return (sac.size() == 0);
    }

    public void retireJoueurDuSac(Joueur joueur){
        sac.remove(joueur);
    }

    public Integer getNbreJoueurParEquipe(){
        switch (this.typeTournoi){
            case SIMPLE: return 1;
            case DOUBLE: return 2;
            default: return null;
        }
    }

    public void mettreJoueurDansSac(){
        sac = new ArrayList<Joueur>();
        for (int i = 0; i < listJoueurs.size() ; i++){
            if (listJoueurs.get(i).isActif() && !listJoueursAttribues.contains(listJoueurs.get(i)))
                sac.add(listJoueurs.get(i));
        }
    }


}
