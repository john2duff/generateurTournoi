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
    private final Tab context;
    private final Tab points;
    private final Tab regles;
    private final VBox vboxContext;
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
    private final GridPane vboxAvantage;
    private final GridPane vboxComptagePoint;
    private final VBox vboxRegle;
    private ConfigToolBar configToolBar;
    private final TabPane tab;
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

        //pane
        tab = new TabPane();
        tab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        context = new Tab();
        context.setText("Contexte");
        points = new Tab();
        points.setText("Points");
        regles = new Tab();
        regles.setText("Règles");
        tab.getTabs().addAll(context, points, regles);

        //context
        ScrollPane spContext = new ScrollPane();
        spContext.setPadding(new Insets(5,5,5,5));
        vboxContext = new VBox();
        vboxContext.setSpacing(5);
        vboxContext.setAlignment(Pos.CENTER_LEFT);
        vboxContext.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        spContext.setFitToWidth(true);
        spContext.setContent(vboxContext);
        context.setContent(spContext);

        //attribution des points
        ScrollPane spAttribution = new ScrollPane();
        spAttribution.setPadding(new Insets(5,5,5,5));
        VBox both = new VBox();
        both.setSpacing(10);
        both.setFillWidth(true);

        TitledPane titledPaneAvantage = new TitledPane();
        titledPaneAvantage.setText("Points avantage et handicap");
        titledPaneAvantage.setCollapsible(true);

        vboxAvantage = new GridPane();
        vboxAvantage.setVgap(5);
        vboxAvantage.setHgap(5);
        vboxAvantage.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        titledPaneAvantage.setContent(vboxAvantage);

        TitledPane titledPanePoints = new TitledPane();
        titledPanePoints.setText("Comptage des points");
        titledPanePoints.setCollapsible(true);
        vboxComptagePoint = new GridPane();
        vboxComptagePoint.setVgap(5);
        vboxComptagePoint.setHgap(5);
        titledPanePoints.setContent(vboxComptagePoint);

        both.getChildren().addAll(titledPanePoints, titledPaneAvantage);
        spAttribution.setFitToWidth(true);
        spAttribution.setContent(both);
        points.setContent(spAttribution);

        //regles
        ScrollPane spRegle = new ScrollPane();
        spRegle.setPadding(new Insets(5,5,5,5));
        vboxRegle = new VBox();
        vboxRegle.setSpacing(5);
        vboxRegle.setStyle("-fx-font-size: "+ ctrl.getTailleTexte() +";");
        spRegle.setFitToWidth(true);
        spRegle.setContent(vboxRegle);
        regles.setContent(spRegle);

        setCenter(tab);

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
        lTypeTournoi.setStyle("-fx-min-width: 200;");
        if (!configToolBar.getModeEdition()){
            Label typeTournoi = new Label(tournoi.getTypeTournoiToString());
            typeTournoi.setStyle("-fx-font-weight: bold;");
            hboxTypeTournoi.getChildren().addAll(lTypeTournoi, typeTournoi);
        }else{
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
        lNombreTerrain.setStyle("-fx-min-width: 200;");
        if (!configToolBar.getModeEdition()){
            Label nombreTerrain = new Label(tournoi.getNombreTerrainToString());
            nombreTerrain.setStyle("-fx-font-weight: bold;");
            hboxNombreTerrain.getChildren().addAll(lNombreTerrain, nombreTerrain);
        }else{
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
        lNombreTour.setStyle("-fx-min-width: 200;");
        if (!configToolBar.getModeEdition()){
            Label nombreTour = new Label(tournoi.getNombreTourToString());
            nombreTour.setStyle("-fx-font-weight: bold;");
            hboxNombreTour.getChildren().addAll(lNombreTour, nombreTour);
        }else{
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
            hboxNiveau.setAlignment(Pos.CENTER_LEFT);
            Label lNomNiveau = new Label(Niveau.getNiveaux().get(i).getNomNiveau());
            lNomNiveau.setStyle("-fx-min-width: 50;");
            if (!configToolBar.getModeEdition()){
                Label avantage = new Label(Niveau.getNiveaux().get(i).getPointsToString());
                avantage.setStyle("-fx-font-weight: bold; -fx-min-width: 50;");
                hboxNiveau.getChildren().addAll(lNomNiveau, avantage);
            }else{
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
        hboxVictoire.setAlignment(Pos.CENTER_LEFT);
        Label lVictoire = new Label("Victoire");
        lVictoire.setStyle("-fx-min-width: 70;");
        if (!configToolBar.getModeEdition()){
            Label victoire = new Label(tournoi.getVictoireToString());
            victoire.setStyle("-fx-font-weight: bold;");
            hboxVictoire.getChildren().addAll(lVictoire, victoire);
        }else{
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
        lDefaitePlus.setStyle("-fx-min-width: 70;");
        if (!configToolBar.getModeEdition()){
            Label defaitePlus = new Label(tournoi.getDefaitePlusToString());
            defaitePlus.setStyle("-fx-font-weight: bold;");
            hboxDefaitePlus.getChildren().addAll(lDefaitePlus, defaitePlus);
        }else{
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
        lDefaite.setStyle("-fx-min-width: 70;");
        if (!configToolBar.getModeEdition()){
            Label defaite = new Label(tournoi.getDefaiteToString());
            defaite.setStyle("-fx-font-weight: bold;");
            hboxDefaite.getChildren().addAll(lDefaite, defaite);
        }else{
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
