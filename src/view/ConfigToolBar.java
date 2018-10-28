package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

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
                refreshModeEdition();
            }
        });

        modeEdition = new ToggleButton("Modifier", ctrl.chargeImageView("/img/edit.png"));
        modeEdition.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.modeEditionConfigChange(getModeEdition());
                refreshModeEdition();
            }
        });
        refreshModeEdition();

        genererTournoi.setAlignment(Pos.CENTER_RIGHT);

        getItems().addAll(modeEdition, genererTournoi, arreterTournoi);

    }

    public void refreshModeEdition(){
        if (!ctrl.tournoiEnCours()){
            modeEdition.setDisable(false);
            genererTournoi.setDisable(false);
            arreterTournoi.setVisible(false);
            if (modeEdition.isSelected()){
                modeEdition.setText("Valider les modifications");
                genererTournoi.setVisible(false);
            }else{
                modeEdition.setText("Modifier");
                genererTournoi.setVisible(true);
            }
        }else{
            modeEdition.setDisable(true);
            genererTournoi.setVisible(false);
            arreterTournoi.setVisible(true);

        }
    }

    public boolean getModeEdition() {
        return modeEdition.isSelected();
    }

}
