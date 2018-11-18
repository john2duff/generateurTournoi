package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Match implements Serializable {


    public enum Equipe {EQUIPE_A, EQUIPE_B;};

    private ArrayList<Joueur> equipeA;

    private ArrayList<Joueur> equipeB;
    boolean redondanceEquipier;
    boolean redondanceAdversaire;
    boolean ecartMaxi;
    boolean equipeMixte;
    private Integer scoreEquipeA;
    private Integer scoreEquipeB;
    private final Integer numeroMatch;

    public Match(Integer indexMatch) {
        equipeA = new ArrayList<>();
        equipeB = new ArrayList<>();
        redondanceAdversaire = false;
        redondanceEquipier = false;
        ecartMaxi = false;
        equipeMixte = false;
        this.numeroMatch = indexMatch;
    }

    public boolean victoire(Equipe equipe) {
        if (equipe == Equipe.EQUIPE_A){
            return scoreEquipeA > scoreEquipeB && (scoreEquipeA-scoreEquipeB) >=2;
        }else{
            return scoreEquipeB > scoreEquipeA && (scoreEquipeB-scoreEquipeA) >=2;
        }
    }

    public boolean defaitePlus(Equipe equipe) {
        if (equipe == Equipe.EQUIPE_A){
            return scoreEquipeA < scoreEquipeB && scoreEquipeB >= 22;
        }else{
            return scoreEquipeB < scoreEquipeA && scoreEquipeA >= 22;
        }
    }

    public boolean defaite(Equipe equipe) {
        if (equipe == Equipe.EQUIPE_A){
            return scoreEquipeA < scoreEquipeB;
        }else{
            return scoreEquipeB < scoreEquipeA;
        }
    }

    public boolean isRedondanceEquipier() {
        return redondanceEquipier;
    }

    public boolean isRedondanceAdversaire() {
        return redondanceAdversaire;
    }

    public boolean isEcartMaxi() {
        return ecartMaxi;
    }

    public boolean isEquipeMixte() {
        return equipeMixte;
    }

    public void setInitialScore() {
        Integer scoreA = getInitialScoreEquipeA();
        Integer scoreB = getInitialScoreEquipeB();
        //equilibrage
        if (scoreA <= scoreB){
            scoreEquipeA = scoreA - scoreB;
            scoreEquipeB = 0;
        }else{
            scoreEquipeB = scoreB - scoreA;
            scoreEquipeA = 0;
        }
    }

    public void setScoreEquipeA(Integer score) {
        scoreEquipeA = score;
    }
    public void setScoreEquipeB(Integer score) {
        scoreEquipeB = score;
    }


    public Integer getNumeroMatch() {
        return numeroMatch;
    }

    public boolean estFini() {
        return (getScoreEquipeA() >= 21 || getScoreEquipeB() >= 21) && Math.abs(getScoreEquipeA() - getScoreEquipeB()) >= 2;
    }

    public Integer getScoreEquipeA() {
        return scoreEquipeA;
    }

    public Integer getScoreEquipeB() {
        return scoreEquipeB;
    }

    public Integer getInitialScoreEquipeA() {
        Integer points = 0;
        for (int i = 0; i < equipeA.size(); i++){
            points += equipeA.get(i).getPointsHandicap();
        }
        return points;
    }

    public Integer getInitialScoreEquipeB() {
        Integer points = 0;
        for (int i = 0; i < equipeB.size(); i++){
            points += equipeB.get(i).getPointsHandicap();
        }
        return points;
    }

    public void ajouteJoueurEquipe(Joueur joueur, Equipe equipe){
        switch(equipe){
            case EQUIPE_A:
                equipeA.add(joueur);
                propagerJoueur(joueur, equipe);
                break;
            case EQUIPE_B:
                equipeB.add(joueur);
                propagerJoueur(joueur, equipe);
        }
    }

    public void propagerJoueur(Joueur joueur, Equipe equipe){
        switch(equipe){
            case EQUIPE_A:
                //ajout du nouveau joueur dans chaque coéquipier
                for (int i = 0; i < equipeA.size(); i++){
                    equipeA.get(i).addEquipier(joueur);
                }
                //ajout de chaque coéquipier dans nouveau joueur
                for (int i = 0; i < equipeA.size(); i++){
                    joueur.addEquipier(equipeA.get(i));
                }
                //ajout de chaque adversaire dans nouveau joueur
                for (int i = 0; i < equipeB.size(); i++){
                    equipeB.get(i).addAdversaire(joueur);
                }
                //ajout du nouveau joueur dans chaque adversaire
                for (int i = 0; i < equipeB.size(); i++){
                    joueur.addAdversaire(equipeB.get(i));
                }
                break;
            case EQUIPE_B:
                //ajout du nouveau joueur dans chaque coéquipier
                for (int i = 0; i < equipeB.size(); i++){
                    equipeB.get(i).addEquipier(joueur);
                }
                //ajout de chaque coéquipier dans nouveau joueur
                for (int i = 0; i < equipeB.size(); i++){
                    joueur.addEquipier(equipeB.get(i));
                }
                //ajout de chaque adversaire dans nouveau joueur
                for (int i = 0; i < equipeA.size(); i++){
                    equipeA.get(i).addAdversaire(joueur);
                }
                //ajout du nouveau joueur dans chaque adversaire
                for (int i = 0; i < equipeA.size(); i++){
                    joueur.addAdversaire(equipeA.get(i));
                }
                break;
        }
    }

    public boolean redondanceAdversaire(Joueur joueurAlea, Equipe equipe) {
        boolean retour = false;
        switch(equipe){
            case EQUIPE_A:
                for (int i = 0; i < equipeB.size() ; i++){
                    if (equipeB.get(i).aDejaJoueContre(joueurAlea)){
                        retour = true;
                    }
                }
                break;
            case EQUIPE_B:
                for (int i = 0; i < equipeA.size() ; i++){
                    if (equipeA.get(i).aDejaJoueContre(joueurAlea)){
                        retour = true;
                    }
                }
                break;
        }
        redondanceAdversaire = retour;
        return retour;
    }

    public boolean redondanceEquipier(Joueur joueurAlea, Equipe equipe) {
        boolean retour = false;
        switch(equipe){
            case EQUIPE_A:
                for (int i = 0; i < equipeA.size() ; i++){
                    if (equipeA.get(i).aDejaJoueAvec(joueurAlea)){
                        retour = true;
                    }
                }
                break;
            case EQUIPE_B:
                for (int i = 0; i < equipeB.size() ; i++){
                    if (equipeB.get(i).aDejaJoueAvec(joueurAlea)){
                        retour = true;
                    }
                }
                break;
        }
        redondanceEquipier = retour;
        return retour;
    }

    public boolean equipeMixte(Joueur joueurAlea) {
        boolean equipeAIsMixte = false;
        boolean equipeBIsMixte = false;

        boolean retour;
        if (equipeA.size() == 1 && equipeB.size() == 2){
            if(equipeA.get(0).getSexe() != joueurAlea.getSexe())
                equipeAIsMixte = true;
            if(equipeB.get(0).getSexe() != equipeB.get(1).getSexe())
                equipeBIsMixte = true;
        }else if (equipeA.size() == 2 && equipeB.size() == 2){
            if(equipeA.get(0).getSexe() != joueurAlea.getSexe())
                equipeAIsMixte = true;
            if(equipeB.get(0).getSexe() != equipeB.get(1).getSexe())
                equipeBIsMixte = true;
        }

        if ((equipeAIsMixte == true && equipeBIsMixte == false) ||
                (equipeAIsMixte == false && equipeBIsMixte == true)){
            retour = true;
        }else{
            retour = false;
        }

        return retour;
    }

    public boolean ecartMaxi(Joueur joueurAlea) {
        return false;
    }

    public ArrayList<Joueur> getEquipeA() {
        return equipeA;
    }

    public ArrayList<Joueur> getEquipeB() {
        return equipeB;
    }
}
