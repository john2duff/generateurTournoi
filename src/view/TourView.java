package view;

import controler.Controler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Tour;

public class TourView extends Tab {

    private final Controler ctrl;
    private final Tour tour;

    TourView(Controler ctrl, Tour tour){
        this.ctrl = ctrl;
        this.tour = tour;
        setText("Tour " + (tour.getNumeroTour()+1));
        ScrollPane sp = new ScrollPane();
        sp.setPadding(new Insets(5,5,5,5));
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        FlowPane vTour = new FlowPane();
        vTour.setHgap(5);
        vTour.setVgap(5);
        vTour.setAlignment(Pos.TOP_LEFT);
        //vTour.setSpacing(10);
        //vTour.setFillWidth(true);

        if (ctrl.getCurrentTour() == null || tour.getNumeroTour() < ctrl.getCurrentTour()){
            setStyle("-fx-background-color: rgb(196,196,196);");
            vTour.setStyle("-fx-background-color: rgb(196,196,196);");
            sp.setStyle("-fx-background-color: rgb(196,196,196);");
        }
        for (int j = 0; j < tour.getListMatchs().size(); j++){
            MatchView match = new MatchView(ctrl, tour.getListMatchs().get(j), tour);
            if (ctrl.getCurrentTour() == null || tour.getNumeroTour() < ctrl.getCurrentTour()) {
                match.setStyle("-fx-background-color: rgb(196,196,196);");
            }
                vTour.getChildren().add(match);
        }
        //joueur restant
        if (tour.getJoueurRestant().size() > 0){
            TitledPane joueurRestant = new TitledPane();
            joueurRestant.setText("Joueur(s) restant(s) : ");
            VBox listJoueurRestant = new VBox();
            if (tour.getAlerteJoueurAttente() && ctrl.getCurrentTournoi().isShowContrainte()){
                listJoueurRestant.getChildren().add(ctrl.chargeImageView("/img/contrainte-attente.png"));
            }
            for (int j = 0; j < tour.getJoueurRestant().size(); j++){
                JoueurCellFactory jLigne = new JoueurCellFactory(tour.getJoueurRestant().get(j));
                listJoueurRestant.getChildren().add(jLigne);
            }
            joueurRestant.setContent(listJoueurRestant);
            vTour.getChildren().add(joueurRestant);
        }
        sp.setContent(vTour);
        setContent(sp);
    }



}
