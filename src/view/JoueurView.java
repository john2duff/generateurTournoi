package view;

import controler.Controler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Joueur;
import model.Niveau;
import model.Tournoi;
import sun.plugin.javascript.navig.Anchor;

import java.util.ArrayList;

public class JoueurView extends BorderPane {

    private final ScrollPane container;
    private Controler ctrl;
    private JoueurToolbar joueurToolbar;
    private VBox vboxJoueur = new VBox();

    public JoueurView(Controler ctrl) {

        getStylesheets().add(getClass().getResource("Style/Style.css").toExternalForm());
        this.ctrl = ctrl;

        //top
        joueurToolbar = new JoueurToolbar(ctrl);
        setTop(joueurToolbar);

        //center
        container = new ScrollPane();
        container.setPadding(new Insets(5,5,5,5));
        container.setContent(vboxJoueur);
        vboxJoueur.setSpacing(5);
        container.setFitToWidth(true);

        setCenter(container);
    }

    public void refreshListJoueur(ArrayList<Joueur> listJoueurs) {
        vboxJoueur.getChildren().clear();
        //ajout joueurs
        for (int i = 0; i < listJoueurs.size(); i++){
            JoueurCellFactory jLigne = new JoueurCellFactory(joueurToolbar.getModeEdition(), listJoueurs.get(i), ctrl, i);
            vboxJoueur.getChildren().add(jLigne);
        }
    }

    public void enregistreListeJoueurs() {

    }

    public ArrayList<Joueur> getListJoueurs() {
        ArrayList<Joueur> joueurs = new ArrayList<>();
        for (int i = 0; i < vboxJoueur.getChildren().size(); i++){
            String prenom = ((JoueurCellFactory)vboxJoueur.getChildren().get(i)).getPrenomEdition();
            String nom = ((JoueurCellFactory)vboxJoueur.getChildren().get(i)).getNomEdition();
            boolean actif = ((JoueurCellFactory)vboxJoueur.getChildren().get(i)).getActif();
            Joueur.Sexe sexe;
            if (((JoueurCellFactory)vboxJoueur.getChildren().get(i)).getSexeEdition().equals("Homme"))
                sexe = Joueur.Sexe.HOMME;
            else
                sexe = Joueur.Sexe.FEMME;
            Niveau niveau = Niveau.getNiveau(((JoueurCellFactory)vboxJoueur.getChildren().get(i)).getNiveauEdition());
            joueurs.add(new Joueur(prenom, nom, niveau, sexe, actif));
        }
        return joueurs;
    }

}
