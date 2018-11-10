package view;

import com.sun.org.apache.xml.internal.serializer.ToHTMLSAXHandler;
import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import model.Tour;

public class TournoiToolBar extends ToolBar {

    private final Button distribuerJoueur;
    private final Controler ctrl;
    private final HBox hContrainte;
    private ToggleButton attenteJoueur;
    private ToggleButton redondanceEquipier;
    private ToggleButton redondanceAdversaire;
    private ToggleButton equipeMixte;
    private ToggleButton ecartMaxi;
    private Button clotureTour;


    public TournoiToolBar(Controler ctrl) {

        this.ctrl = ctrl;

        clotureTour = new Button("");
        clotureTour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.clotureTour();
            }
        });

        //mis de côté pour l'instant
        distribuerJoueur = new Button("Redistribuer les joueurs");
        distribuerJoueur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.redistribuerJoueur();
            }
        });

        hContrainte = new HBox();
        attenteJoueur = new ToggleButton("", ctrl.chargeImageView("/img/contrainte-attente.png"));
        attenteJoueur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.changeContrainteShow();
            }
        });
        redondanceAdversaire = new ToggleButton("", ctrl.chargeImageView("/img/contrainte-adversaire.png"));
        redondanceAdversaire.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.changeContrainteShow();
            }
        });
        redondanceEquipier = new ToggleButton("", ctrl.chargeImageView("/img/contrainte-equipier.png"));
        redondanceEquipier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.changeContrainteShow();
            }
        });
        equipeMixte = new ToggleButton("", ctrl.chargeImageView("/img/contrainte-mixte.png"));
        equipeMixte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.changeContrainteShow();
            }
        });
        ecartMaxi = new ToggleButton("", ctrl.chargeImageView("/img/scoreboard.png"));
        ecartMaxi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.changeContrainteShow();
            }
        });
        hContrainte.getChildren().addAll(attenteJoueur, redondanceAdversaire, redondanceEquipier, equipeMixte, ecartMaxi);

        getItems().addAll(clotureTour, hContrainte);
    }

    public ToggleButton getAttenteJoueur() {
        return attenteJoueur;
    }

    public ToggleButton getRedondanceEquipier() {
        return redondanceEquipier;
    }

    public ToggleButton getRedondanceAdversaire() {
        return redondanceAdversaire;
    }

    public ToggleButton getEquipeMixte() {
        return equipeMixte;
    }

    public ToggleButton getEcartMaxi() {
        return ecartMaxi;
    }

    public void refresh(){
        Integer currentTour = ctrl.getCurrentTournoi().getCurrentTour();
        if (ctrl.tournoiEnCours()){
            if (currentTour == null){
                clotureTour.setText("Tournoi terminé");
                clotureTour.setDisable(true);
            }else {
                Tour tour = ctrl.getCurrentTournoi().getListTours().get(currentTour);
                if (currentTour == ctrl.getCurrentTournoi().getListTours().size() - 1) {
                    clotureTour.setText("Clôturer le tournoi");
                }else {
                    clotureTour.setText("Clôturer le tour " + (currentTour+1));
                }
                if (tour.isCloturable()) clotureTour.setDisable(false); else clotureTour.setDisable(true);
            }
        }else{
            clotureTour.setText("Aucun tournoi en cours");
            clotureTour.setDisable(true);
        }

        hContrainte.setVisible(ctrl.tournoiEnCours());

        attenteJoueur.setSelected(ctrl.getCurrentTournoi().contrainteIsVisible("Attente joueur"));
        attenteJoueur.setVisible(ctrl.getCurrentTournoi().contrainteIsActif("Attente joueur"));
        redondanceAdversaire.setSelected(ctrl.getCurrentTournoi().contrainteIsVisible("Redondance adversaire"));
        redondanceAdversaire.setVisible(ctrl.getCurrentTournoi().contrainteIsActif("Redondance adversaire"));
        redondanceEquipier.setSelected(ctrl.getCurrentTournoi().contrainteIsVisible("Redondance équipier"));
        redondanceEquipier.setVisible(ctrl.getCurrentTournoi().contrainteIsActif("Redondance équipier"));
        equipeMixte.setSelected(ctrl.getCurrentTournoi().contrainteIsVisible("Equipe mixte"));
        equipeMixte.setVisible(ctrl.getCurrentTournoi().contrainteIsActif("Equipe mixte"));
        ecartMaxi.setSelected(ctrl.getCurrentTournoi().contrainteIsVisible("Ecart maxi"));
        ecartMaxi.setVisible(ctrl.getCurrentTournoi().contrainteIsActif("Ecart maxi"));

    }

}
