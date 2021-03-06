package view;

import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Joueur;
import model.Niveau;

import javax.swing.*;


public class JoueurCellFactory extends HBox {

    private final CheckBox actif = new CheckBox();
    private ImageView photo;
    private final Label prenom = new Label();
    private final Label nom = new Label();
    private final Label niveau = new Label();
    private final Label points = new Label();
    private final Label pointsEcart = new Label();
    private final Label classement = new Label();

    private final TextField nomEdition = new TextField();
    private final TextField prenomEdition = new TextField();
    private final Button sexeEdition = new Button();
    private final ComboBox niveauEdition = new ComboBox();

    private Controler ctrl;
    private Button supprimerEdition;

    public JoueurCellFactory(boolean modeEdition, Joueur j, Controler ctrl, Integer index) {
        this.ctrl = ctrl;
        photo = ctrl.chargePhoto(j);
        getChildren().clear();
        if (!modeEdition){
            //datas
            actif.setSelected(j.getActif());
            actif.setId(index.toString());
            actif.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    ctrl.selectionJoueur(((CheckBox)e.getSource()).getId(), ((CheckBox)e.getSource()).isSelected());
                }
            });
            VBox nomPrenom = new VBox();
            prenom.setText(j.getPrenom());
            nom.setText(j.getNom());
            nomPrenom.setSpacing(5);
            nomPrenom.setFillWidth(true);
            if(prenom.getText().equals("") && nom.getText().equals("")){
                nomPrenom.getChildren().addAll(prenom);
            }else if(prenom.getText().equals("")){
                nomPrenom.getChildren().addAll(nom);
            }else if(nom.getText().equals("")){
                nomPrenom.getChildren().addAll(prenom);
            }else {
                nomPrenom.getChildren().addAll(prenom, nom);
            }
            nomPrenom.setAlignment(Pos.CENTER);
            niveau.setText(j.getNiveau().getNomNiveau());
            points.setText(j.getPoints().toString());
            pointsEcart.setText(j.getPointsEcart().toString());
            if (!ctrl.tournoiEnCours()){
                setPrefWidth(300d);
                getChildren().addAll(actif, photo, nomPrenom, niveau);
            }else{
                setPrefWidth(400d);
                classement.setText(String.valueOf(index+1));
                getChildren().addAll(classement, photo, nomPrenom, niveau, points, pointsEcart);
            }
            setId(index.toString());
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    ((JoueurCellFactory)me.getSource()).setActif(!(((JoueurCellFactory)me.getSource()).getActif()));
                    ctrl.selectionJoueur((((JoueurCellFactory)me.getSource()).getActifCheckbox()).getId(), (((JoueurCellFactory)me.getSource()).getActif()));
                }
            });
        }else{
            //datas
            prenomEdition.setText(j.getPrenom());
            nomEdition.setText(j.getNom());

            sexeEdition.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if (sexeEdition.getText().equals("Homme")){
                        sexeEdition.setText("Femme");
                    }else{
                        sexeEdition.setText("Homme");
                    }
                    ctrl.clickSexe();
                }
            });
            niveauEdition.getItems().addAll(Niveau.getNiveauxToString());
            niveauEdition.getSelectionModel().select(j.getNiveau().getNomNiveau());
            supprimerEdition = new Button("Supprimer", ctrl.chargeImageView("/img/minus.png"));
            supprimerEdition.setId(index.toString());
            supprimerEdition.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    ctrl.supprimerJoueur(((Button)e.getSource()).getId());
                }
            });

            getChildren().addAll(photo, prenomEdition, nomEdition, sexeEdition, niveauEdition, supprimerEdition);
        }
        setStyleJoueur(j);
    }

    public JoueurCellFactory (Joueur j){
        setStyleJoueur(j);
        prenom.setText(j.getPrenom());
        nom.setText(j.getNom());
        getChildren().addAll(prenom, nom);
    }

    public void setStyleJoueur(Joueur j){
        getStyleClass().clear();
        if (j.getSexe() == Joueur.Sexe.HOMME){
            sexeEdition.setText("Homme");
            if (j.isActif()) getStyleClass().add("homme"); else getStyleClass().add("joueurItem");
        }else{
            sexeEdition.setText("Femme");
            if (j.isActif()) getStyleClass().add("femme"); else getStyleClass().add("joueurItem");
        }
        actif.setStyle("-fx-min-width: 30; -fx-padding: 10;");
        prenom.setStyle("-fx-min-width: 100;");
        prenom.setAlignment(Pos.CENTER);
        prenomEdition.setStyle("-fx-min-width: 100;");
        nom.setStyle("-fx-min-width: 100;");
        nom.setAlignment(Pos.CENTER);
        nomEdition.setStyle("-fx-min-width: 100;");
        sexeEdition.setStyle("-fx-min-width: 100;");
        niveau.setStyle("-fx-min-width: 70;");
        niveauEdition.setStyle("-fx-min-width: 70;");
        points.setStyle("-fx-min-width: 70;");
        points.setAlignment(Pos.CENTER);
        classement.setStyle("-fx-min-width: 50; -fx-font-weight: bold;");
        classement.setAlignment(Pos.CENTER);
        setPadding(new Insets(5,5,5,5));
        setSpacing(5);
        setAlignment(Pos.CENTER_LEFT);
    }

    private void setActif(boolean b) {
        actif.setSelected(b);
    }


    public String getPrenomEdition(){
        return prenomEdition.getText();
    }
    public String getNomEdition(){
        return nomEdition.getText();
    }
    public String getNiveauEdition(){
        return (String)niveauEdition.getSelectionModel().getSelectedItem();
    }

    public boolean getActif() {
        return actif.isSelected();
    }

    public CheckBox getActifCheckbox() {
        return actif;
    }

    public String getSexeEdition(){
        return sexeEdition.getText();
    }
}
