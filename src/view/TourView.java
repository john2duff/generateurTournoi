package view;

import controler.Controler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
        VBox vTour = new VBox();
        vTour.setSpacing(10);
        vTour.setFillWidth(true);
        for (int j = 0; j < tour.getListMatchs().size(); j++){
            MatchView match = new MatchView(ctrl, tour.getListMatchs().get(j), tour);
            vTour.getChildren().add(match);
        }
        //joueur restant
        TitledPane joueurRestant = new TitledPane();
        for (int j = 0; j < tour.getJoueurRestant().size(); j++){
            JoueurCellFactory jLigne = new JoueurCellFactory(tour.getJoueurRestant().get(j));
            vTour.getChildren().add(jLigne);
        }
        sp.setContent(vTour);
        setContent(sp);
    }



}
