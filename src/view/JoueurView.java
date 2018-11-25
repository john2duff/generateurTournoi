package view;

import controler.Controler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private FlowPane vboxJoueur = new FlowPane();

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
        vboxJoueur.setHgap(5);
        vboxJoueur.setVgap(5);
        vboxJoueur.setAlignment(Pos.TOP_LEFT);
        container.setFitToWidth(true);

        setCenter(container);
    }

    public void refreshJoueurView(ArrayList<Joueur> listJoueurs) {
        vboxJoueur.getChildren().clear();
        //ajout joueurs
        Integer comptClassement = 0;
        for (int i = 0; i < listJoueurs.size(); i++){
            if (!ctrl.tournoiEnCours() || (ctrl.tournoiEnCours() && listJoueurs.get(i).isActif())){
                JoueurCellFactory jLigne = new JoueurCellFactory(joueurToolbar.getModeEdition(), listJoueurs.get(i), ctrl, comptClassement);
                vboxJoueur.getChildren().add(jLigne);
                comptClassement++;
            }
        }
        joueurToolbar.refresh();
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

    public boolean isModeEdition(){
        return joueurToolbar.isModeEdition();
    }

    public void exitModeEdition(){
        joueurToolbar.setModeEdition(false);
    }
}
