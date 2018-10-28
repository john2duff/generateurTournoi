package view;

import controler.Controler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Joueur;
import model.Tournoi;

import java.util.ArrayList;

public class TournoiView extends BorderPane {

    private final TournoiToolBar tournoiToolbar;
    private Tournoi currentTournoi;
    private TabPane tab;
    private Controler ctrl;


    public TournoiView(Controler ctrl) {
        this.ctrl = ctrl;

        tournoiToolbar = new TournoiToolBar(ctrl);
        setTop(tournoiToolbar);

        tab = new TabPane();
        tab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        setCenter(tab);
    }

    public void refreshTourToolBar(){
        tournoiToolbar.refreshTourToolBar();

    }

    public void refreshTournoi() {
        this.currentTournoi = ctrl.getCurrentTournoi();
        tab.getTabs().clear();
        for (int i = 0; i < currentTournoi.getListTours().size(); i++){
            TourView tabTour = new TourView(ctrl, currentTournoi.getListTours().get(i));
            tab.getTabs().add(tabTour);
        }
    }






}
