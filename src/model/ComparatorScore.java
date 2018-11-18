package model;

import java.util.Comparator;

public class ComparatorScore implements Comparator<Joueur> {
    @Override
    public int compare(Joueur o1, Joueur o2) {
        return o1.getPoints().compareTo(o2.getPoints());
    }
}
