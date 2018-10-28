package controler;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.ConfigToolBar;

import java.util.Optional;

public class Generateur extends Application {

    private Controler ctrl;

    @Override
    public void start(Stage primaryStage){
        ctrl = new Controler(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()  {
            @Override
            public void handle(WindowEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ButtonType buttonTypeOui = new ButtonType("Oui");
                ButtonType buttonTypeNon = new ButtonType("Non");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon, buttonTypeCancel);

                alert.setTitle("Enregistrement");
                alert.setHeaderText("Vous allez quitter l'application");
                alert.setContentText("Voulez-vous enregistrer le tournoi en cours ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOui){
                    ctrl.enregistreTournoi();
                }else if (result.get() == buttonTypeNon ) {
                    System.exit(0);
                }else if (result.get() == buttonTypeCancel){
                    t.consume();
                    primaryStage.show();
                }

            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
