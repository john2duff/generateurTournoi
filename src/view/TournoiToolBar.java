package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import model.Tour;

public class TournoiToolBar extends ToolBar {

    private final Button distribuerJoueur;
    private final Controler ctrl;
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

        distribuerJoueur = new Button("Redistribuer les joueurs");
        distribuerJoueur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ctrl.redistribuerJoueur();
            }
        });

        getItems().addAll(clotureTour, distribuerJoueur);
    }

    public void refresh(){
        Integer currentTour = ctrl.getCurrentTournoi().getCurrentTour();
        if (currentTour != null){
            clotureTour.setText("Cloturer le tour " + (currentTour+1));
            if (ctrl.getCurrentTournoi().getListTours().size() > 0) {
                Tour tour = ctrl.getCurrentTournoi().getListTours().get(currentTour);
                if (tour.isCloturable()) {
                    clotureTour.setDisable(false);
                } else {
                    clotureTour.setDisable(true);
                    if (!ctrl.tourIsCloture(tour)) {
                        clotureTour.setVisible(true);
                    } else {
                        clotureTour.setDisable(false);
                    }
                }
            }
        }else{
            clotureTour.setVisible(false);
            distribuerJoueur.setVisible(false);
        }

    }

}
