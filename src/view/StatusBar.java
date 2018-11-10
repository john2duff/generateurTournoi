package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class StatusBar extends AnchorPane {

    private Label textInfo;
    private Label textEnregistrement;

    public StatusBar() {
        //style
        setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-fill: white;");
        textInfo = new Label();
        textInfo.setWrapText(true);
        textInfo.setStyle("-fx-fill: white; -fx-padding: 5;");
        AnchorPane.setLeftAnchor(textInfo, 5d);
        textEnregistrement = new Label();
        textEnregistrement.setStyle("-fx-background-color: lightgray; -fx-fill: white; -fx-padding: 5;");
        AnchorPane.setRightAnchor(textEnregistrement, 0d);
        getChildren().addAll(textInfo, textEnregistrement);
    }

    public void updateInfo(String txt){
        textInfo.setText(txt);
    }
    public void updateEnregistrement(String txt){
        textEnregistrement.setText(txt);
    }
}
