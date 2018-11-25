package model;

import java.util.Comparator;

public class ComparatorScore implements Comparator<Joueur> {
    @Override
    public int compare(Joueur o1, Joueur o2) {
        if (o2.getPoints().equals(o1.getPoints())){
            return o1.getPointsEcart().compareTo(o2.getPointsEcart());
        }else{
            return o2.getPoints().compareTo(o1.getPoints());
        }
    }
}
