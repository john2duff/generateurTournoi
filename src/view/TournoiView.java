package view;

import controler.Controler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Tournoi;

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

    public void refreshTournoi() {
        this.currentTournoi = ctrl.getCurrentTournoi();
        tab.getTabs().clear();
        for (int i = 0; i < currentTournoi.getListTours().size(); i++){
            TourView tabTour = new TourView(ctrl, currentTournoi.getListTours().get(i));
            tab.getTabs().add(tabTour);
        }
        tournoiToolbar.refresh();
    }

    public void refreshTournoiToolbar(){
        tournoiToolbar.refresh();
    }

    public void selectCurrentTabTour() {
        if (currentTournoi.getCurrentTour() != null){
            tab.getSelectionModel().select(currentTournoi.getCurrentTour());
        }
    }
}
