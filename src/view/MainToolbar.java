package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;


public class MainToolbar extends ToolBar {

    private Controler ctrl;
    private Button nouveau;
    private Button enregistrer;
    private Button ouvrir;
    private Button renameTournoi;
    private CheckBox enregistrementAuto;

    public MainToolbar(Controler ctrl) {

        this.ctrl = ctrl;

        nouveau = new Button("Nouveau", ctrl.chargeImageView("/img/file.png"));
        nouveau.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ctrl.nouveauTournoi();
            }
        });

        enregistrer = new Button("Enregistrer", ctrl.chargeImageView("/img/save.png"));
        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ctrl.enregistreTournoi();
            }
        });

        enregistrementAuto = new CheckBox ("Enregistrement automatique");
        enregistrementAuto.setSelected(true);
        enregistrementAuto.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ctrl.saveAuto(enregistrementAuto.isSelected());
                refresh();
            }
        });

        renameTournoi = new Button("Renommer le tournoi");
        renameTournoi.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ctrl.renommerTournoi();
            }
        });

        ouvrir = new Button("Ouvrir", ctrl.chargeImageView("/img/folder.png"));
        ouvrir.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ctrl.ouvrirTournoi();
            }
        });

        getItems().addAll(nouveau, ouvrir, enregistrer, renameTournoi, enregistrementAuto);

    }

    public void refresh() {
        if (enregistrementAuto.isSelected()){
            enregistrer.setDisable(true);
        }else{
            enregistrer.setDisable(false);
        }
    }
}
