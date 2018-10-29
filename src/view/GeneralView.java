package view;

import controler.Controler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.Joueur;
import model.Tournoi;

import java.util.ArrayList;

public class GeneralView extends BorderPane {

    private Controler ctrl;
    private MainToolbar toolBarPrincipal;

    private ConfigView vueConfig;
    private JoueurView vueJoueur;
    private TournoiView vueTournoi;

    private TabPane tab;
    private Tab tabJoueur;
    private Tab tabConfig;
    private Tab tabTournoi;
    private StatusBar statusBar;

    public GeneralView(Controler ctrl) {

        this.ctrl = ctrl;

        vueJoueur = new JoueurView(ctrl);
        vueConfig = new ConfigView(ctrl);
        vueTournoi = new TournoiView(ctrl);

        //center
        tab = new TabPane();
        tab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabJoueur = new Tab();
        tabJoueur.setText("Joueurs");
        tabJoueur.setContent(vueJoueur);
        tabConfig = new Tab();
        tabConfig.setText("Configuration");
        tabConfig.setContent(vueConfig);
        tabTournoi = new Tab();
        tabTournoi.setText("Tournoi");
        tabTournoi.setContent(vueTournoi);

        tab.getTabs().addAll(tabJoueur, tabConfig, tabTournoi);
        setCenter(tab);

        //top
        toolBarPrincipal = new MainToolbar(ctrl);
        setTop(toolBarPrincipal);

        //bottom
        statusBar = new StatusBar();
        setBottom(statusBar);
    }

    public JoueurView getVueJoueur() {
        return vueJoueur;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void ouvreTournoi(){
        refreshTournoi();
    }

    public void refreshTournoi(){
        refreshJoueurView();
        refreshConfigView();
        refreshTournoiView();
        refreshMainToolbar();
    }

    public void refreshMainToolbar(){
        toolBarPrincipal.refresh();
    }

    public void refreshJoueurView() {
        vueJoueur.refreshJoueurView(ctrl.getListJoueurs());
    }

    public void refreshConfigView() {
        vueConfig.refreshConfig(ctrl.getCurrentTournoi());
    }

    public void refreshTournoiView() {
        vueTournoi.refreshTournoi();
    }

    public ArrayList<Joueur> getListJoueurs() {
        return vueJoueur.getListJoueurs();
    }

    public Tournoi getTournoiConfig() {
        return vueConfig.getTournoiConfig();
    }


    public void selectTabTournoi() {
        tab.getSelectionModel().select(2);
    }
}
