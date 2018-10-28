package view;

import controler.Controler;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Joueur;
import model.Match;
import model.Tour;

import java.util.ArrayList;

public class MatchView extends TitledPane {

    private final Controler ctrl;
    private final Match match;
    private final Tour tour;

    private final int minScore = -40;
    private final int maxScore = 40;
    private final Spinner<Integer> scoreEquipeA;
    private final Spinner<Integer> scoreEquipeB;

    public MatchView(Controler ctrl, Match match, Tour tour) {
        this.ctrl = ctrl;
        this.match = match;
        this.tour = tour;
        setText("Match " + (match.getNumeroMatch()+1));
        HBox vMatch = new HBox();
        vMatch.setAlignment(Pos.CENTER);
        vMatch.setSpacing(10);

        VBox hJoueurEquipeA = createVueEquipe(match.getEquipeA(), false);
        VBox hJoueurEquipeB = createVueEquipe(match.getEquipeB(), true);

        scoreEquipeA = createScore(match.getScoreEquipeA(), tour.getNumeroTour());
        scoreEquipeA.valueProperty().addListener((obs, oldValue, newValue) ->
                ctrl.scoreChange(tour.getNumeroTour(), match.getNumeroMatch(), newValue));
        scoreEquipeB = createScore(match.getScoreEquipeA(), tour.getNumeroTour());

        ImageView volant = ctrl.chargeImageView("/img/main.png");
        volant.setFitHeight(30.0);
        volant.setFitWidth(30.0);

        vMatch.getChildren().addAll(hJoueurEquipeA, scoreEquipeA, volant, scoreEquipeB, hJoueurEquipeB);
        setContent(vMatch);
    }

    public VBox createVueEquipe(ArrayList<Joueur> equipe, boolean droite){
        VBox vboxEquipe = new VBox();
        vboxEquipe.setSpacing(5);
        for (int k = 0; k < equipe.size(); k++){
            HBox hEquipe = new HBox();
            Joueur joueur = equipe.get(k);
            hEquipe.setAlignment(Pos.CENTER_LEFT);
            hEquipe.setSpacing(5);
            ImageView photo = ctrl.chargePhoto(joueur);
            VBox vNomPrenom = new VBox();
            Label prenom = new Label(joueur.getPrenom());
            prenom.setStyle("-fx-min-width: 100; -fx-text-alignment: center;");
            Label nom = new Label(joueur.getNom());
            nom.setStyle("-fx-min-width: 100;-fx-text-alignment: center;");
            vNomPrenom.getChildren().addAll(prenom, nom);
            Label niveau = new Label(joueur.getNiveau().getNomNiveau());
            if (droite){
                prenom.setAlignment(Pos.CENTER_LEFT);
                nom.setAlignment(Pos.CENTER_LEFT);
                hEquipe.getChildren().addAll(photo, niveau, vNomPrenom);
            }else{
                prenom.setAlignment(Pos.CENTER_RIGHT);
                nom.setAlignment(Pos.CENTER_RIGHT);
                hEquipe.getChildren().addAll(vNomPrenom, niveau, photo);
            }
            vboxEquipe.getChildren().addAll(hEquipe);
        }
        return vboxEquipe;
    }

    public Spinner<Integer> createScore(Integer scoreEquipe, Integer indexTour){
        Spinner<Integer> scoreEquipeSpinner = new Spinner<>(minScore, maxScore, scoreEquipe);
        scoreEquipeSpinner.setStyle("-fx-max-width: 60; -fx-min-width: 60;");
        scoreEquipeSpinner.setDisable(!indexTour.equals(tour.getNumeroTour()));
        scoreEquipeSpinner.setEditable(true);
        return scoreEquipeSpinner;
    }
}
