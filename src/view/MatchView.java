package view;

import controler.Controler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.RotateEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.Joueur;
import model.Match;
import model.Tour;

import javax.swing.*;
import java.util.ArrayList;

public class MatchView extends TitledPane {

    private final Controler ctrl;
    private final Match match;
    private final Tour tour;

    private final int minScore = -40;
    private final int maxScore = 40;
    private final HBox vMatch;
    private Spinner<Integer> scoreEquipeA;
    private Spinner<Integer> scoreEquipeB;
    private Label scoreEquipeALabel;
    private Label scoreEquipeBLabel;
    private final VBox hJoueurEquipeA;
    private final VBox hJoueurEquipeB;

    public MatchView(Controler ctrl, Match match, Tour tour) {
        this.ctrl = ctrl;
        this.match = match;
        this.tour = tour;
        setText("Match " + (match.getNumeroMatch()+1));
        vMatch = new HBox();
        vMatch.setAlignment(Pos.CENTER);
        vMatch.setSpacing(10);

        if (ctrl.getCurrentTour() == null || tour.getNumeroTour() < ctrl.getCurrentTour()){
            vMatch.setStyle("-fx-background-color: rgb(196,196,196);");
        }else{
            vMatch.setStyle("-fx-background-color: none;");
        }

        hJoueurEquipeA = createVueEquipe(match.getEquipeA(), false);
        hJoueurEquipeB = createVueEquipe(match.getEquipeB(), true);

        VBox centre = new VBox();
        HBox contrainte = new HBox();
        if (match.isRedondanceAdversaire())contrainte.getChildren().add(new ImageView("/img/contrainte-adversaire.png"));
        if (match.isRedondanceEquipier())contrainte.getChildren().add(new ImageView("/img/contrainte-equipier.png"));
        if (match.isEquipeMixte())contrainte.getChildren().add(new ImageView("/img/contrainte-mixte.png"));
        if (match.isEcartMaxi())contrainte.getChildren().add(new ImageView("/img/scoreboard.png"));
        ImageView volant = ctrl.chargeImageView("/img/main.png");
        volant.setFitHeight(30.0);
        volant.setFitWidth(30.0);
        centre.setAlignment(Pos.CENTER);
        if (!ctrl.tournoiEnCours()) {
            centre.getChildren().addAll(volant);
        }else if (ctrl.getCurrentTournoi().isShowContrainte()){
            centre.getChildren().addAll(contrainte, volant);
        }else{
            centre.getChildren().addAll(volant);
        }

        if (ctrl.getCurrentTour() == null || tour.getNumeroTour() < ctrl.getCurrentTour()){
            scoreEquipeALabel = new Label(String.valueOf(match.getScoreEquipeA()));
            scoreEquipeALabel.setAlignment(Pos.CENTER);
            scoreEquipeALabel.setStyle("-fx-max-width: 80; -fx-min-width: 80; -fx-font-size: 2em;");
            scoreEquipeBLabel = new Label(String.valueOf(match.getScoreEquipeB()));
            scoreEquipeBLabel.setAlignment(Pos.CENTER);
            scoreEquipeBLabel.setStyle("-fx-max-width: 80; -fx-min-width: 80; -fx-font-size: 2em;");
            vMatch.getChildren().addAll(hJoueurEquipeA, scoreEquipeALabel, centre, scoreEquipeBLabel, hJoueurEquipeB);
        }else{
            scoreEquipeA = createScore(match.getScoreEquipeA(), tour.getNumeroTour(), true);
            scoreEquipeA.valueProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable,Integer oldValue, Integer newValue) {
                    ctrl.scoreChange(tour.getNumeroTour(), match.getNumeroMatch(), newValue, true);
                    coloriseMatch();
                }
            });
            scoreEquipeB = createScore(match.getScoreEquipeB(), tour.getNumeroTour(), false);
            scoreEquipeB.valueProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable,Integer oldValue, Integer newValue) {
                    ctrl.scoreChange(tour.getNumeroTour(), match.getNumeroMatch(), newValue, false);
                    coloriseMatch();
                }
            });
            vMatch.getChildren().addAll(hJoueurEquipeA, scoreEquipeA, centre, scoreEquipeB, hJoueurEquipeB);
        }

        coloriseMatch();
        setContent(vMatch);
    }

    private void coloriseMatch() {
        if((match.getScoreEquipeA() >= 21 || match.getScoreEquipeB() >= 21) && Math.abs(match.getScoreEquipeA() - match.getScoreEquipeB()) >= 2){
            vMatch.setStyle("-fx-background-color: rgb(196,196,196);");

            if (match.getScoreEquipeA() > match.getScoreEquipeB()){
                hJoueurEquipeA.setStyle("-fx-background-color: rgb(190,229,90); -fx-border-radius: 50px;");
                hJoueurEquipeB.setStyle("-fx-background-color: rgb(244,154,154);-fx-border-radius: 50px;");
            }else{
                hJoueurEquipeA.setStyle("-fx-background-color: rgb(244,154,154);-fx-border-radius: 50px;");
                hJoueurEquipeB.setStyle("-fx-background-color: rgb(190,229,90);-fx-border-radius: 50px;");
            }
            for (int i = 0; i < match.getEquipeA().size(); i++){
                if (match.getEquipeA().get(i).getSexe() == Joueur.Sexe.HOMME){
                    hJoueurEquipeA.getChildren().get(i).setStyle("-fx-background-color: none;");
                }else{
                    hJoueurEquipeA.getChildren().get(i).setStyle("-fx-background-color: none;");
                }
            }
            for (int i = 0; i < match.getEquipeB().size(); i++){
                if (match.getEquipeB().get(i).getSexe() == Joueur.Sexe.HOMME){
                    hJoueurEquipeB.getChildren().get(i).setStyle("-fx-background-color: none;");
                }else{
                    hJoueurEquipeB.getChildren().get(i).setStyle("-fx-background-color: none;");
                }
            }
        }else{
            vMatch.setStyle("-fx-background-color: none;");

            for (int i = 0; i < match.getEquipeA().size(); i++){
                if (match.getEquipeA().get(i).getSexe() == Joueur.Sexe.HOMME){
                    hJoueurEquipeA.getChildren().get(i).setStyle("-fx-background-color: rgba(102, 153, 255, 0.5);");
                }else{
                    hJoueurEquipeA.getChildren().get(i).setStyle("-fx-background-color: rgba(255, 153, 204, 0.5);");
                }
            }
            for (int i = 0; i < match.getEquipeB().size(); i++){
                if (match.getEquipeB().get(i).getSexe() == Joueur.Sexe.HOMME){
                    hJoueurEquipeB.getChildren().get(i).setStyle("-fx-background-color: rgba(102, 153, 255, 0.5);");
                }else{
                    hJoueurEquipeB.getChildren().get(i).setStyle("-fx-background-color: rgba(255, 153, 204, 0.5);");
                }
            }
            hJoueurEquipeA.setStyle("-fx-background-color: none;");
            hJoueurEquipeB.setStyle("-fx-background-color: none;");
        }
    }

    public VBox createVueEquipe(ArrayList<Joueur> equipe, boolean droite){
        VBox vboxEquipe = new VBox();
        vboxEquipe.setSpacing(5);
        vboxEquipe.setPadding(new Insets(10, 10, 10, 10));
        for (int k = 0; k < equipe.size(); k++){
            HBox hEquipe = new HBox();
            hEquipe.setPadding(new Insets(10, 10, 10, 10));
            Joueur joueur = equipe.get(k);
            hEquipe.setAlignment(Pos.CENTER_LEFT);
            hEquipe.setSpacing(10);
            ImageView photo = ctrl.chargePhoto(joueur);
            VBox vNomPrenom = new VBox();
            Label prenom = new Label(joueur.getPrenom());
            prenom.setAlignment(Pos.CENTER);
            Label nom = new Label(joueur.getNom());
            nom.setAlignment(Pos.CENTER);
            if(prenom.getText().equals("") && nom.getText().equals("")){
                vNomPrenom.getChildren().addAll(prenom);
            }else if(prenom.getText().equals("")){
                vNomPrenom.getChildren().addAll(nom);
            }else if(nom.getText().equals("")){
                vNomPrenom.getChildren().addAll(prenom);
            }else {
                vNomPrenom.getChildren().addAll(prenom, nom);
            }
            vNomPrenom.setStyle("-fx-min-width: 100;");
            vNomPrenom.setAlignment(Pos.CENTER);

            Label niveau = new Label(joueur.getNiveau().getNomNiveau());
            niveau.setStyle("-fx-min-width: 30;");
            niveau.setAlignment(Pos.CENTER);
            if (droite){
                hEquipe.getChildren().addAll(photo, niveau, vNomPrenom);
            }else{
                hEquipe.getChildren().addAll(vNomPrenom, niveau, photo);
            }
            vboxEquipe.getChildren().addAll(hEquipe);
        }
        return vboxEquipe;
    }

    public Spinner<Integer> createScore(Integer scoreEquipe, Integer indexTour, boolean equipeA){
        Spinner<Integer> scoreEquipeSpinner = new Spinner<>(minScore, maxScore, scoreEquipe);
        scoreEquipeSpinner.setStyle("-fx-text-alignment: RIGHT; -fx-max-width: 80; -fx-min-width: 80; -fx-font-size: 2em;");
        if (equipeA){
            scoreEquipeSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL);
        }
        scoreEquipeSpinner.setDisable(!indexTour.equals(ctrl.getCurrentTour()));
        scoreEquipeSpinner.setEditable(false);
        return scoreEquipeSpinner;
    }

}
