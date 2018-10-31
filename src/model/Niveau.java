package model;

import javafx.scene.Node;

import java.io.Serializable;
import java.util.ArrayList;

public class Niveau implements Serializable {

    private String nomNiveau;
    private String categorie;
    private Integer points;
    public static ArrayList<Niveau> niveaux = Niveau.loadNiveau();

    public Niveau(String nomNiveau, String categorie, Integer points) {
        this.nomNiveau = nomNiveau;
        this.categorie = categorie;
        this.points = points;
    }

    public static ArrayList<Niveau> getNiveaux() {
        return niveaux;
    }

    public static ArrayList<String> getNiveauxToString() {
        ArrayList<String> retour = new ArrayList<>();
        for (int i = 0; i < niveaux.size(); i++){
            retour.add(niveaux.get(i).nomNiveau);
        }
        return retour;
    }

    public String getNomNiveau() {
        return nomNiveau;
    }

    public static Niveau getNiveau(String niveauString){
        for (int i = 0; i < niveaux.size(); i++){
            if (niveaux.get(i).getNomNiveau().equals(niveauString))
                return niveaux.get(i);
        }
        return null;
    }

    public static ArrayList<Niveau> loadNiveau(){
        niveaux = new ArrayList<>();
        niveaux.add(new Niveau("P12", "Promotion", 0));
        niveaux.add(new Niveau("P11", "Promotion", -2));
        niveaux.add(new Niveau("P10", "Promotion", -4));
        niveaux.add(new Niveau("D9", "Départemental", -6));
        niveaux.add(new Niveau("D8", "Départemental", -8));
        niveaux.add(new Niveau("D7", "Départemental", -10));
        niveaux.add(new Niveau("R6", "Régional", -12));
        niveaux.add(new Niveau("R5", "Régional", -14));
        niveaux.add(new Niveau("R4", "Régional", -16));
        niveaux.add(new Niveau("N3", "National", -18));
        niveaux.add(new Niveau("N2", "National", -20));
        niveaux.add(new Niveau("N1", "National", -22));
        niveaux.add(new Niveau("Fille", "Aucun", 2));
        return niveaux;
    }

    public String getPointsToString() {
        return points.toString();
    }
    public Integer getPoints() {
        return points;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }


}
