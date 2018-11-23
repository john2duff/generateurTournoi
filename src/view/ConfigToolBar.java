package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

import java.io.FileNotFoundException;

public class ConfigToolBar extends ToolBar {

    private final Controler ctrl;
    private final ToggleButton modeEdition;
    private final Button arreterTournoi;
    private Button genererTournoi;


    public ConfigToolBar(Controler ctrl) {

        this.ctrl = ctrl;

        genererTournoi = new Button("Générer tournoi");
        genererTournoi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.genererTournoi();
            }
        });

        arreterTournoi = new Button("Arrêter tournoi", ctrl.chargeImageView("/img/stop.png"));
        arreterTournoi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.arreterTournoi();
            }
        });

        modeEdition = new ToggleButton("Modifier", ctrl.chargeImageView("/img/edit.png"));
        modeEdition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.modeEditionConfigChange(getModeEdition());
            }
        });
        refresh();

        genererTournoi.setAlignment(Pos.CENTER_RIGHT);

        getItems().addAll(modeEdition, genererTournoi, arreterTournoi);

    }

    public void refresh(){
        if (!ctrl.tournoiEnCours()){
            arreterTournoi.setVisible(false);
            if (ctrl.getNbreJoueurSuffisant()){
                if(modeEdition.isSelected() || ctrl.isJoueurModeEdition()){
                    genererTournoi.setDisable(true);
                    genererTournoi.setText("Générer tournoi -> Modifications en cours...");
                }else{
                    genererTournoi.setDisable(false);
                    genererTournoi.setText("Générer tournoi");
                }
            }else{
                genererTournoi.setDisable(true);
                genererTournoi.setText("Générer tournoi -> Nombre de joueurs insuffisant !");
            }

            modeEdition.setDisable(false);
            if (modeEdition.isSelected()){
                modeEdition.setText("Valider les modifications");
            }else{
                modeEdition.setText("Modifier");
            }
        }else{
            modeEdition.setDisable(true);
            genererTournoi.setDisable(true);
            arreterTournoi.setVisible(true);
        }
    }

    public boolean getModeEdition() {
        return modeEdition.isSelected();
    }

    public void setModeEdition(boolean b) {
        modeEdition.setSelected(false);
    }
}
