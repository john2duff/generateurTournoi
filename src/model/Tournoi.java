package model;

import com.sun.deploy.security.ValidationState;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Tournoi implements Serializable {

    private String nomTournoi;

    public enum TypeTournoi {SIMPLE, DOUBLE, BOTH}

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
    public Boolean tournoiEnCours;
    public Boolean showContrainte;
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
        this.tournoiEnCours = false;
        this.showContrainte = false;
        loadContraintes();
    }
    public void actualiserPoints(){
        initScoreJoueurs();
        for (int i = 0; i < listTours.size(); i++){
            for (int j = 0; j < listTours.get(i).getListMatchs().size(); j++){
                if (listTours.get(i).getListMatchs().get(j).estFini()){
                    distribuePoints(listTours.get(i).getListMatchs().get(j));
                }
            }
        }
    }

    private void distribuePoints(Match match) {
        //equipe A
        for(int i = 0; i < match.getEquipeA().size(); i++){
            if (match.victoire(Match.Equipe.EQUIPE_A)){
                match.getEquipeA().get(i).ajoutePoints(this.pointVictoire);
            }else if (match.defaitePlus(Match.Equipe.EQUIPE_A)){
                match.getEquipeA().get(i).ajoutePoints(this.pointDefaitePlus);
                match.getEquipeA().get(i).ajoutePointsEcart(match.getScoreEquipeB()-match.getScoreEquipeA());
            }else if (match.defaite(Match.Equipe.EQUIPE_A)){
                match.getEquipeA().get(i).ajoutePoints(this.pointDefaite);
                match.getEquipeA().get(i).ajoutePointsEcart(match.getScoreEquipeB()-match.getScoreEquipeA());
            }
        }
        //equipe B
        for(int i = 0; i < match.getEquipeB().size(); i++){
            if (match.victoire(Match.Equipe.EQUIPE_B)){
                match.getEquipeB().get(i).ajoutePoints(this.pointVictoire);
            }else if (match.defaitePlus(Match.Equipe.EQUIPE_B)){
                match.getEquipeB().get(i).ajoutePoints(this.pointDefaitePlus);
                match.getEquipeB().get(i).ajoutePointsEcart(match.getScoreEquipeA()-match.getScoreEquipeB());
            }else if (match.defaite(Match.Equipe.EQUIPE_B)){
                match.getEquipeB().get(i).ajoutePoints(this.pointDefaite);
                match.getEquipeB().get(i).ajoutePointsEcart(match.getScoreEquipeA()-match.getScoreEquipeB());
            }
        }
    }

    private void initScoreJoueurs() {
        for (int i = 0; i < listJoueurs.size(); i++){
            listJoueurs.get(i).setPoints(0);
            listJoueurs.get(i).setPointsEcart(0);
        }
    }

    public boolean isTourCloture(Tour tour){
        return currentTour >= tour.getNumeroTour();
    }

    public boolean isShowContrainte() {
        return showContrainte;
    }

    public void clotureTour() {
        if (currentTour < listTours.size()-1){
            currentTour++;
        }else{
            currentTour = null;
            System.out.println("tournoi fini");
        }
    }

    public void changeScore(Integer numeroTour, Integer numeroMatch, Integer newValue, boolean equipeA) {
        if (equipeA){
            listTours.get(numeroTour).getListMatchs().get(numeroMatch).setScoreEquipeA(Integer.parseInt(String.valueOf(newValue)));
        }else{
            listTours.get(numeroTour).getListMatchs().get(numeroMatch).setScoreEquipeB(Integer.parseInt(String.valueOf(newValue)));
        }
    }

    public void loadContraintes(){
        listContraintes = new ArrayList<>();
        listContraintes.add(new Contrainte("Attente joueur", "On évite que les joueurs reste sur la touche pendant plusieurs tours.",
                true, false, TypeTournoi.BOTH, "/img/contrainte-attente.png"));
        listContraintes.add(new Contrainte("Redondance équipier", "Le tirage au sort des équipe évite que l'on rejoue plusieurs fois avec le même équipier dans le cas d'un match en double.",
                true, false, TypeTournoi.DOUBLE, "/img/contrainte-equipier.png"));
        listContraintes.add(new Contrainte("Redondance adversaire", "Le tirage au sort des équipe évite que l'on rejoue plusieurs fois contre le même adversaire.",
                true, false, TypeTournoi.BOTH, "/img/contrainte-adversaire.png"));
        listContraintes.add(new Contrainte("Equipe mixte", "On ne permet pas qu'une équipe mixte puisse jouer avec une équipe non mixte.",
                true, false, TypeTournoi.DOUBLE, "/img/contrainte-mixte.png"));
        listContraintes.add(new Contrainte("Ecart maxi", "On ne permet pas un écart de points de plus de X points entre les deux équipes.",
                true,false, TypeTournoi.BOTH, "/img/scoreboard.png"));
    }

    public Contrainte getContrainte(String nomContrainte){
        for (int i = 0; i < listContraintes.size(); i++){
            if (listContraintes.get(i).getNom().equals(nomContrainte))
                return listContraintes.get(i);
        }
        return null;
    }

    public Boolean isTournoiEnCours() {
        return tournoiEnCours;
    }

    public void setTournoiEnCours(boolean tournoiEnCours) {
        this.tournoiEnCours = tournoiEnCours;
        if (tournoiEnCours == false){
            this.listTours = new ArrayList<Tour>();
        }
    }

    public void changeContrainteShow(boolean contrainteShow) {
        showContrainte = contrainteShow;
    }

    public Integer getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour(Integer tour) {
        currentTour = tour;
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
        trier();
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

    public ArrayList<Niveau> getListNiveau() {
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
        boolean retour;
        if (!listJoueurs.contains(j)){
            retour = true;
        }else{
            retour = false;
        }
        listJoueurs.add(j);
        trier();
        return retour;
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

    public void initJoueurs(){
        for (int i = 0; i < listJoueurs.size(); i++){
            listJoueurs.get(i).init();
        }
    }

    public boolean generer(){
        Joueur joueurTireAuSort;
        //si le tournoi n'a pas commencé
        if (currentTour == null){
            currentTour = 0;
        }
        tournoiEnCours = true;

        initJoueurs();

        listJoueursAttente = new ArrayList<>();
        listTours = new ArrayList<>();

        if (getNbreJoueurSuffisant()){
            for (int i = 0; i < nombreTour ; i++){
                Tour tour = new Tour(i);
                listJoueursAttribues = new ArrayList<Joueur>();
                mettreJoueurDansSac(false);
                Integer compt = 0;
                do {
                    Match match = new Match(compt);
                    //equipe A joueur 1
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_A, tour, getContrainte("Attente joueur").isActif());
                        if (joueurTireAuSort != null){
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_A);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                    //equipe B joueur 1
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_B, tour, getContrainte("Attente joueur").isActif());
                        if (joueurTireAuSort != null) {
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_B);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }

                    if (typeTournoi == TypeTournoi.DOUBLE){
                        //equipe A joueur 2
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_A, tour, getContrainte("Attente joueur").isActif());
                        if (joueurTireAuSort != null){
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_A);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                        //equipe B joueur 2
                        joueurTireAuSort = tireAuSort(match, getLastContrainte(), Match.Equipe.EQUIPE_B, tour, getContrainte("Attente joueur").isActif());
                        if (joueurTireAuSort != null) {
                            match.ajouteJoueurEquipe(joueurTireAuSort, Match.Equipe.EQUIPE_B);
                            listJoueursAttribues.add(joueurTireAuSort);
                        }
                    }
                    match.setInitialScore();
                    if (match.getEquipeA().size() > 0)
                        tour.ajouteMatch(match);
                    mettreJoueurDansSac(false);
                    compt++;
                } while (terrainRestant(tour) && !sacVide() && getNbreJoueurSuffisantDansSac());
                tour.setJoueurRestant(sac);
                for (int m = 0; m < sac.size(); m++){
                    if (listJoueursAttente.contains(sac.get(m))){
                        tour.setAlerteJoueurAttente(true);
                    }else{
                        listJoueursAttente.add(sac.get(m));
                    }
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

    public boolean getNbreJoueurSuffisant() {
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

    public Joueur tireAuSort(Match match, Integer contrainteIgnore, Match.Equipe equipe, Tour tour, boolean contrainteAttente){
        int alea;
        mettreJoueurDansSac(contrainteAttente);
        while (!sacVide()) {
            alea = (int) (Math.random() * sac.size());
            Joueur joueurAlea = sac.get(alea);
            if (contrainteBloquante(match, joueurAlea, contrainteIgnore, equipe)){
                retireJoueurDuSac(joueurAlea);
            }else{
                return joueurAlea;
            }
        }
        if (contrainteAttente){
            return tireAuSort(match, getLastContrainte(), equipe, tour, false);
        }
        if (contrainteIgnore == 0 ){
            mettreJoueurDansSac(false);
            if (sac.size() == 0){
                return null;
            }else{
                alea = (int) (Math.random() * sac.size());
                return sac.get(alea);
            }
        }else{
            contrainteIgnore --;
            return tireAuSort(match, contrainteIgnore, equipe, tour, false);
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
                return match.ecartMaxi(joueurAlea, equipe, ecartMaxi);
            case "Attente joueur":
                //gérer précedemment
                return false;
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

    public void mettreJoueurDansSac(boolean enAttente){
        sac = new ArrayList<Joueur>();
        if (!enAttente || listJoueursAttente.size() == 0){
            for (int i = 0; i < listJoueurs.size() ; i++){
                if (listJoueurs.get(i).isActif() && !listJoueursAttribues.contains(listJoueurs.get(i)))
                    sac.add(listJoueurs.get(i));
            }
        }else{
            for (int i = 0; i < listJoueursAttente.size() ; i++){
                if (!listJoueursAttribues.contains(listJoueursAttente.get(i)))
                    sac.add(listJoueursAttente.get(i));
            }
        }
    }

    public void trier(){
        if (!isTournoiEnCours()){
            ComparatorPrenom c1 = new ComparatorPrenom();
            Collections.sort(listJoueurs, c1);
        }else{
            ComparatorScore c2 = new ComparatorScore();
            Collections.sort(listJoueurs, c2);
        }
    }

}
