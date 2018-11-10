package view;

import com.sun.javafx.image.IntPixelGetter;
import controler.Controler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Contrainte;
import model.Niveau;
import model.Tournoi;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ConfigView extends BorderPane {


    private final Controler ctrl;
    private final TitledPane context;
    private final TitledPane regles;
    private final FlowPane vboxContext;
    private final Integer minNombreTerrain = 1;
    private final Integer maxNombreTerrain = 20;
    private final Integer minNombreTour = 1;
    private final Integer maxNombreTour = 20;
    private final Integer minHandicap = -10;
    private final Integer maxAvantage = 10;
    private final Integer minPointVictoire = -10;
    private final Integer maxPointVictoire = 10;
    private final Integer minPointDefaitePlus = -10;
    private final Integer maxPointDefaitePlus = 10;
    private final Integer minPointDefaite = -10;
    private final Integer maxPointDefaite = 10;
    private final Integer minEcartMaxi = 10;
    private final Integer maxEcartMaxi = 20;
    private final FlowPane vboxAvantage;
    private final FlowPane vboxComptagePoint;
    private final VBox vboxRegle;
    private ConfigToolBar configToolBar;
    private RadioButton tournoiSimple;
    private RadioButton tournoiDouble;
    private Spinner<Integer> nombreTerrainSpinner;
    private Spinner<Integer> nombreTourSpinner;
    private Spinner<Integer> avantageSpinner;
    private Spinner<Integer> victoireSpinner;
    private Spinner<Integer> defaitePlusSpinner;
    private Spinner<Integer> defaiteSpinner;
    private Spinner<Integer> ecartMaxiSpinner;

    public ConfigView(Controler ctrl) {

        this.ctrl = ctrl;

        //top
        configToolBar = new ConfigToolBar(ctrl);
        setTop(configToolBar);

        //center
        ScrollPane spConfig = new ScrollPane();
        spConfig.setPadding(new Insets(5,5,5,5));

        VBox centre = new VBox();
        centre.setAlignment(Pos.TOP_LEFT);
        centre.setSpacing(5);
        context = new TitledPane();
        context.setText("Contexte");
        //context
        vboxContext = new FlowPane();
        vboxContext.setVgap(5);
        vboxContext.setHgap(5);
        vboxContext.setAlignment(Pos.CENTER_LEFT);
        vboxContext.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        vboxContext.setPrefWrapLength(200d);
        context.setContent(vboxContext);

        TitledPane titledPanePoints = new TitledPane();
        titledPanePoints.setText("Comptage des points");
        titledPanePoints.setCollapsible(true);
        vboxComptagePoint = new FlowPane();
        vboxComptagePoint.setVgap(5);
        vboxComptagePoint.setHgap(5);
        vboxComptagePoint.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        titledPanePoints.setContent(vboxComptagePoint);

        TitledPane titledPaneAvantage = new TitledPane();
        titledPaneAvantage.setText("Points avantage et handicap");
        titledPaneAvantage.setCollapsible(true);
        vboxAvantage = new FlowPane();
        vboxAvantage.setVgap(5);
        vboxAvantage.setHgap(5);
        vboxAvantage.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        titledPaneAvantage.setContent(vboxAvantage);

        regles = new TitledPane();
        regles.setText("Contraintes");
        vboxRegle = new VBox();
        vboxRegle.setSpacing(5);
        vboxRegle.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        regles.setContent(vboxRegle);

        centre.getChildren().addAll(context, titledPanePoints, titledPaneAvantage, regles);

        spConfig.setContent(centre);
        spConfig.setFitToWidth(true);
        setCenter(spConfig);
    }

    public void refreshConfig(Tournoi tournoi){
        refreshContext(tournoi);
        refreshAttributionPoint(tournoi);
        refreshRegle(tournoi);
        configToolBar.refresh();
    }

    public void refreshContext(Tournoi tournoi){
        vboxContext.getChildren().clear();
        //typeTournoi
        HBox hboxTypeTournoi = new HBox();
        hboxTypeTournoi.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxTypeTournoi.setPadding(new Insets(5,5,5,5));
        hboxTypeTournoi.setAlignment(Pos.CENTER_LEFT);
        Label lTypeTournoi = new Label("Type de tournoi");
        if (!configToolBar.getModeEdition()){
            hboxTypeTournoi.setPrefWidth(200d);
            Label typeTournoi = new Label(tournoi.getTypeTournoiToString());
            typeTournoi.setAlignment(Pos.CENTER);
            typeTournoi.setStyle("-fx-font-weight: bold;");
            hboxTypeTournoi.getChildren().addAll(lTypeTournoi, typeTournoi);
        }else{
            hboxTypeTournoi.setPrefWidth(300d);
            ToggleGroup groupType = new ToggleGroup();
            tournoiSimple = new RadioButton("Simple");
            tournoiDouble = new RadioButton("Double");
            tournoiSimple.setToggleGroup(groupType);
            tournoiDouble.setToggleGroup(groupType);
            if (tournoi.getTypeTournoi() == Tournoi.TypeTournoi.SIMPLE){
                tournoiSimple.setSelected(true);
            }else{
                tournoiDouble.setSelected(true);
            }
            hboxTypeTournoi.getChildren().addAll(lTypeTournoi, tournoiSimple, tournoiDouble);
        }
        hboxTypeTournoi.setSpacing(15);

        //Nombre terrain
        HBox hboxNombreTerrain = new HBox();
        hboxNombreTerrain.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxNombreTerrain.setPadding(new Insets(5,5,5,5));
        hboxNombreTerrain.setAlignment(Pos.CENTER_LEFT);
        Label lNombreTerrain = new Label("Nombre de terrain");
        if (!configToolBar.getModeEdition()){
            hboxNombreTerrain.setPrefWidth(200d);
            Label nombreTerrain = new Label(tournoi.getNombreTerrainToString());
            nombreTerrain.setAlignment(Pos.CENTER);
            nombreTerrain.setStyle("-fx-font-weight: bold;");
            hboxNombreTerrain.getChildren().addAll(lNombreTerrain, nombreTerrain);
        }else{
            hboxNombreTerrain.setPrefWidth(300d);
            nombreTerrainSpinner = new Spinner<>(minNombreTerrain, maxNombreTerrain, tournoi.getNombreTerrain());
            nombreTerrainSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            nombreTerrainSpinner.setStyle("-fx-pref-width: 100;");
            hboxNombreTerrain.getChildren().addAll(lNombreTerrain, nombreTerrainSpinner);
        }
        hboxNombreTerrain.setSpacing(15);

        //Nombre tour
        HBox hboxNombreTour = new HBox();
        hboxNombreTour.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxNombreTour.setPadding(new Insets(5,5,5,5));
        hboxNombreTour.setAlignment(Pos.CENTER_LEFT);
        Label lNombreTour = new Label("Nombre de tour");
        if (!configToolBar.getModeEdition()){
            hboxNombreTour.setPrefWidth(200d);
            Label nombreTour = new Label(tournoi.getNombreTourToString());
            nombreTour.setAlignment(Pos.CENTER);
            nombreTour.setStyle("-fx-font-weight: bold;");
            hboxNombreTour.getChildren().addAll(lNombreTour, nombreTour);
        }else{
            hboxNombreTour.setPrefWidth(300d);
            nombreTourSpinner = new Spinner<>(minNombreTour, maxNombreTour, tournoi.getNombreTour());
            nombreTourSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            nombreTourSpinner.setStyle("-fx-pref-width: 100;");
            hboxNombreTour.getChildren().addAll(lNombreTour, nombreTourSpinner);
        }
        hboxNombreTour.setSpacing(15);

        vboxContext.getChildren().addAll(hboxTypeTournoi, hboxNombreTerrain, hboxNombreTour);
    }

    public void refreshAttributionPoint (Tournoi tournoi){
        vboxAvantage.getChildren().clear();
        int col = 0;
        int lig = 0;
        for (int i = 0; i < Niveau.getNiveaux().size(); i++){
            HBox hboxNiveau = new HBox();
            hboxNiveau.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
            hboxNiveau.setPadding(new Insets(5,5,5,5));
            hboxNiveau.setAlignment(Pos.CENTER);
            hboxNiveau.setSpacing(10);
            Label lNomNiveau = new Label(Niveau.getNiveaux().get(i).getNomNiveau());
            if (!configToolBar.getModeEdition()){
                hboxNiveau.setPrefWidth(100d);
                Label avantage = new Label(Niveau.getNiveaux().get(i).getPointsToString());
                avantage.setAlignment(Pos.CENTER);
                avantage.setStyle("-fx-font-weight: bold; -fx-min-width: 50;");
                hboxNiveau.getChildren().addAll(lNomNiveau, avantage);
            }else{
                hboxNiveau.setPrefWidth(150d);
                avantageSpinner = new Spinner<>(minHandicap, maxAvantage, Niveau.getNiveaux().get(i).getPoints());
                avantageSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
                avantageSpinner.setStyle("-fx-pref-width: 80;");
                hboxNiveau.getChildren().addAll(lNomNiveau, avantageSpinner);
            }
            GridPane.setConstraints(hboxNiveau, col, lig);
            vboxAvantage.getChildren().add(hboxNiveau);
            if (col == 2){
                col = 0;
                lig++;
            }else{
                col++;
            }

        }

        vboxComptagePoint.getChildren().clear();
        HBox hboxVictoire = new HBox();
        hboxVictoire.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxVictoire.setPadding(new Insets(5,5,5,5));
        hboxVictoire.setAlignment(Pos.CENTER);
        Label lVictoire = new Label("Victoire");
        if (!configToolBar.getModeEdition()){
            hboxVictoire.setPrefWidth(200d);
            Label victoire = new Label(tournoi.getVictoireToString());
            victoire.setAlignment(Pos.CENTER);
            victoire.setStyle("-fx-font-weight: bold;-fx-min-width: 50;");
            hboxVictoire.getChildren().addAll(lVictoire, victoire);
        }else{
            hboxVictoire.setPrefWidth(200d);
            victoireSpinner = new Spinner<>(minPointVictoire, maxPointVictoire, tournoi.getVictoire());
            victoireSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            victoireSpinner.setStyle("-fx-pref-width: 80;");
            hboxVictoire.getChildren().addAll(lVictoire, victoireSpinner );
        }
        GridPane.setConstraints(hboxVictoire, 0, 0);

        HBox hboxDefaitePlus = new HBox();
        hboxDefaitePlus.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxDefaitePlus.setPadding(new Insets(5,5,5,5));
        hboxDefaitePlus.setAlignment(Pos.CENTER_LEFT);
        Label lDefaitePlus = new Label("DefaitePlus");
        if (!configToolBar.getModeEdition()){
            hboxDefaitePlus.setPrefWidth(200d);
            Label defaitePlus = new Label(tournoi.getDefaitePlusToString());
            defaitePlus.setAlignment(Pos.CENTER);
            defaitePlus.setStyle("-fx-font-weight: bold;-fx-min-width: 50;");
            hboxDefaitePlus.getChildren().addAll(lDefaitePlus, defaitePlus);
        }else{
            hboxDefaitePlus.setPrefWidth(200d);
            defaitePlusSpinner = new Spinner<>(minPointDefaitePlus, maxPointDefaitePlus, tournoi.getDefaitePlus());
            defaitePlusSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            defaitePlusSpinner.setStyle("-fx-pref-width: 80;");
            hboxDefaitePlus.getChildren().addAll(lDefaitePlus, defaitePlusSpinner );
        }
        GridPane.setConstraints(hboxDefaitePlus, 1, 0);

        HBox hboxDefaite = new HBox();
        hboxDefaite.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
        hboxDefaite.setPadding(new Insets(5,5,5,5));
        hboxDefaite.setAlignment(Pos.CENTER_LEFT);
        Label lDefaite = new Label("Defaite");
        if (!configToolBar.getModeEdition()){
            hboxDefaite.setPrefWidth(200d);
            Label defaite = new Label(tournoi.getDefaiteToString());
            defaite.setAlignment(Pos.CENTER);
            defaite.setStyle("-fx-font-weight: bold;-fx-min-width: 50;");
            hboxDefaite.getChildren().addAll(lDefaite, defaite);
        }else{
            hboxDefaite.setPrefWidth(200d);
            defaiteSpinner = new Spinner<>(minPointDefaite, maxPointDefaite, tournoi.getDefaite());
            defaiteSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
            defaiteSpinner.setStyle("-fx-pref-width: 80;");
            hboxDefaite.getChildren().addAll(lDefaite, defaiteSpinner );
        }
        GridPane.setConstraints(hboxDefaite, 2, 0);

        vboxComptagePoint.getChildren().addAll(hboxVictoire, hboxDefaitePlus, hboxDefaite);

    }

    public void refreshRegle(Tournoi tournoi){
        vboxRegle.getChildren().clear();
        for (Integer i = 0; i < tournoi.getListContraintes().size(); i++){

            HBox hboxRegle = new HBox();
            hboxRegle.setStyle("-fx-background-color: rgba(128, 128, 128, 0.5); -fx-border-color: gray;");
            hboxRegle.setPadding(new Insets(5,5,5,5));
            hboxRegle.setAlignment(Pos.CENTER_LEFT);
            Label lOrdreRegle = new Label(""+ (vboxRegle.getChildren().size()+1));
            lOrdreRegle.setStyle("-fx-min-width: 20;");
            Label lNomRegle = new Label(tournoi.getListContraintes().get(i).getNom());
            lNomRegle.setAlignment(Pos.CENTER);
            lNomRegle.setStyle("-fx-min-width: 200; -fx-text-alignment: center;");
            ImageView photo =  ctrl.chargeImageView(tournoi.getListContraintes().get(i).getUrlPhoto());
            photo.setFitHeight(30.0);
            photo.setFitWidth(30.0);
            photo.setStyle("-fx-padding: 10;");
            if (tournoi.getContrainte(lNomRegle.getText()).getNom().equals("Ecart maxi")){
                ecartMaxiSpinner = new Spinner<>(minEcartMaxi, maxEcartMaxi, tournoi.getEcartMaxi());
                ecartMaxiSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
                ecartMaxiSpinner.setStyle("-fx-pref-width: 80; -fx-min-width: 80;");
                tournoi.getListContraintes().get(i).setDescription("On ne permet pas un écart de points de plus de " + tournoi.getEcartMaxi() + " points entre les deux équipes.");
            }
            Label lDescriptionRegle = new Label(tournoi.getListContraintes().get(i).getDescription());
            lDescriptionRegle.setWrapText(true);
            if (!configToolBar.getModeEdition()){
                hboxRegle.getChildren().addAll(lOrdreRegle, photo, lNomRegle, lDescriptionRegle);
            }else{
                CheckBox actif = new CheckBox();
                actif.setSelected(tournoi.getListContraintes().get(i).isActif());
                Button monter = new Button("",  ctrl.chargeImageView("/img/up-arrow.png"));
                monter.setDisable(i == 0);
                monter.setId(i.toString());
                monter.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            ctrl.monterContrainte(((Button)e.getSource()).getId());
                        }
                    });

                Button descendre = new Button("",  ctrl.chargeImageView("/img/down-arrow.png"));
                descendre.setDisable(i >= tournoi.getListContraintes().size() - 1);
                descendre.setId(i.toString());
                descendre.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        ctrl.descendreContrainte(((Button)e.getSource()).getId());
                    }
                });

                hboxRegle.setSpacing(5);
                if (tournoi.getContrainte(lNomRegle.getText()).getNom().equals("Ecart maxi")){
                    lNomRegle.setStyle("-fx-min-width: 115;");
                    lDescriptionRegle.setText(tournoi.getListContraintes().get(i).getDescription());
                    hboxRegle.getChildren().addAll(actif, lOrdreRegle, photo,  monter, descendre, lNomRegle, ecartMaxiSpinner,  lDescriptionRegle);
                }else{
                    hboxRegle.getChildren().addAll(actif, lOrdreRegle, photo, monter, descendre, lNomRegle, lDescriptionRegle);
                }
            }
            if (tournoi.getListContraintes().get(i).isActif() || configToolBar.getModeEdition())
                vboxRegle.getChildren().add(hboxRegle);
            if (tournoi.getListContraintes().get(i).getTypeTournoi() == Tournoi.TypeTournoi.BOTH ||
                    tournoi.getListContraintes().get(i).getTypeTournoi() == tournoi.getTypeTournoi()){
                hboxRegle.setDisable(false);
            }else{
                hboxRegle.setDisable(true);
            }
        }
    }


    public Tournoi getTournoiConfig() {
        Tournoi tournoi = new Tournoi();
        if (tournoiSimple.isSelected()){
            tournoi.setTypeTournoi(Tournoi.TypeTournoi.SIMPLE);
        }else{
            tournoi.setTypeTournoi(Tournoi.TypeTournoi.DOUBLE);
        }
        tournoi.setNombreTerrain(nombreTerrainSpinner.getValue());
        tournoi.setNombreTour(nombreTourSpinner.getValue());

        ArrayList<Niveau> listNiveau = new ArrayList<>();
        for (int i = 0; i < vboxAvantage.getChildren().size(); i++){
            HBox h = (HBox)vboxAvantage.getChildren().get(i);
            Integer points = (Integer)((Spinner)h.getChildren().get(1)).getValue();
            listNiveau.add(new Niveau("", "", points));
        }
        tournoi.setListNiveau(listNiveau);

        tournoi.setVictoire(victoireSpinner.getValue());
        tournoi.setDefaitePlus(defaitePlusSpinner.getValue());
        tournoi.setDefaite(defaiteSpinner.getValue());

        ArrayList<Contrainte> listContraintes = new ArrayList<>();
        for (int i = 0; i < vboxRegle.getChildren().size(); i++){
            HBox h = (HBox)vboxRegle.getChildren().get(i);
            boolean actif = (boolean)((CheckBox)h.getChildren().get(0)).isSelected();
            String nom = (String)((Label)h.getChildren().get(5)).getText();
            String description = "";
            if (nom.equals("Ecart maxi")){
                tournoi.setEcartMaxi(ecartMaxiSpinner.getValue());
                description = (String)((Label)h.getChildren().get(7)).getText();
            }else{
                description = (String)((Label)h.getChildren().get(6)).getText();
            }
            Contrainte c = new Contrainte(nom, description, actif, false, ctrl.getCurrentTournoi().getContrainte(nom).getTypeTournoi(), ctrl.getCurrentTournoi().getContrainte(nom).getUrlPhoto());
            listContraintes.add(c);
        }
        tournoi.setListContraintes(listContraintes);

        return tournoi;
    }

}
