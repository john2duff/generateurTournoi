package model;

import java.util.Comparator;

public class ComparatorPrenom implements Comparator<Joueur> {
    @Override
    public int compare(Joueur o1, Joueur o2) {
        return o1.getPrenom().compareTo(o2.getPrenom());
    }
}
