package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Tour implements Serializable {

    private ArrayList<Match> listMatchs;
    private ArrayList<Joueur> joueurRestant;
    private final Integer numeroTour;

    public Tour(Integer indexTour) {
        this.listMatchs = new ArrayList<>();
        this.joueurRestant = new ArrayList<>();
        this.numeroTour = indexTour;
    }

    public Integer getNumeroTour() {
        return numeroTour;
    }

    public void ajouteMatch(Match m){
        listMatchs.add(m);
    }

    public ArrayList<Match> getListMatchs() {
        return listMatchs;
    }

    public boolean isCloturable() {
        for (int i = 0; i < listMatchs.size(); i++){
            if (!listMatchs.get(i).estFini()){
                return false;
            }
        }
        return true;
    }

    public void setJoueurRestant(ArrayList<Joueur> sac) {
        joueurRestant = sac;
    }

    public ArrayList<Joueur> getJoueurRestant() {
        return joueurRestant;
    }
}
