package test;

import model.Joueur;
import model.Niveau;
import model.Tournoi;
import org.junit.Assert;
import org.junit.Test;

public class TournoiTest {

    @Test
    public void tournoiSimple(){

        Joueur john = new Joueur("John", "Merandat", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur olivier = new Joueur("Olivier", "Marlot", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur stephane = new Joueur("Stéphane", "Bertin", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur audrey = new Joueur("Audrey", "Vuillemin", Niveau.getNiveau("P12"), Joueur.Sexe.FEMME, true );
        Joueur trop = new Joueur("enplus", "enplus", Niveau.getNiveau("P12"), Joueur.Sexe.FEMME, true );

        Tournoi tournoi = new Tournoi("Test", Tournoi.TypeTournoi.SIMPLE, 5,5);
        tournoi.ajouteJoueur(john);
        tournoi.ajouteJoueur(olivier);
        tournoi.ajouteJoueur(stephane);
        tournoi.ajouteJoueur(audrey);
        tournoi.ajouteJoueur(trop);

        tournoi.generer();

        System.out.println(tournoi);

        Assert.assertTrue(true);
    }

    @Test
    public void tournoiDouble(){

        Joueur john = new Joueur("John", "Merandat", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur olivier = new Joueur("Olivier", "Marlot", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur stephane = new Joueur("Stéphane", "Bertin", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true );
        Joueur audrey = new Joueur("Audrey", "Vuillemin", Niveau.getNiveau("P12"), Joueur.Sexe.FEMME, true );

        Tournoi tournoi = new Tournoi("Test", Tournoi.TypeTournoi.DOUBLE, 5,5);
        tournoi.ajouteJoueur(john);
        tournoi.ajouteJoueur(olivier);
        tournoi.ajouteJoueur(stephane);
        tournoi.ajouteJoueur(audrey);

        tournoi.generer();

        System.out.println(tournoi);

        Assert.assertTrue(true);
    }

}
