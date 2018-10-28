package view;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class StatusBar extends AnchorPane {

    private Label textInfo;

    public StatusBar() {
        //style
        setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-fill: white;");
        textInfo = new Label();
        textInfo.setStyle("-fx-fill: white; -fx-padding: 5;");
        getChildren().addAll(textInfo);
    }

    public void updateInfo(String txt){
        textInfo.setText(txt);
    }
}
