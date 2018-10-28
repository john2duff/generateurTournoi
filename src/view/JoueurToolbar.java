package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

public class JoueurToolbar extends ToolBar {

    private final Controler ctrl;
    private Button ajouterJoueur;
    private Button supprimerTout;
    private ToggleButton modeEdition;
    private Button abort;

    public JoueurToolbar(Controler ctrl) {

        this.ctrl = ctrl;

        abort = new Button();
        abort.setVisible(false);
        abort.setText("Annuler");
        abort.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modeEdition.setSelected(false);
                refreshModeEdition();
                ctrl.abortJoueurChange();
            }
        });

        ajouterJoueur = new Button("", ctrl.chargeImageView("/img/add.png"));
        ajouterJoueur.setText("Ajouter joueur");
        ajouterJoueur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.ajouterJoueur();
            }
        });

        supprimerTout = new Button("Tout supprimer", ctrl.chargeImageView("/img/cancel-music.png"));
        supprimerTout.setText("Tout supprimer");
        supprimerTout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.supprimerTout();
            }
        });

        modeEdition = new ToggleButton("Modifier", ctrl.chargeImageView("/img/edit.png"));
        modeEdition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.modeEditionJoueurChange(getModeEdition());
                refreshModeEdition();
            }
        });
        refreshModeEdition();

        getItems().addAll(modeEdition, ajouterJoueur, supprimerTout);

    }

    public void refreshModeEdition(){
        if (!ctrl.tournoiEnCours()){
            modeEdition.setDisable(false);
            if (modeEdition.isSelected()){
                modeEdition.setText("Valider les modifications");
                abort.setVisible(true);
                supprimerTout.setVisible(true);
                ajouterJoueur.setVisible(true);
            }else{
                modeEdition.setText("Modifier");
                abort.setVisible(false);
                supprimerTout.setVisible(false);
                ajouterJoueur.setVisible(false);
            }
        }else{
            modeEdition.setDisable(true);
            abort.setVisible(false);
            supprimerTout.setVisible(false);
            supprimerTout.setVisible(false);
        }
    }

    public boolean getModeEdition() {
        return modeEdition.isSelected();
    }
}
