package controler;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import view.GeneralView;

import java.io.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorCompletionService;

public class Controler {

    private final String nomNouveauTournoi = "Nouveau tournoi.trn";
    private GeneralView vueGeneral;
    private Stage primaryStage;
    private Tournoi currentTournoi = null;
    private boolean saveAuto = true;
    private final String dossierRacine = "/Tournois/";
    private boolean tournoiAEnregistrer;

    public Controler(Stage primaryStage) {

        /*Joueur john = new Joueur("John", "Merandat", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME);
        Joueur olivier = new Joueur("Olivier", "Marlot", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME);
        Joueur stephane = new Joueur("Stéphane", "Bertin", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME);
        Joueur audrey = new Joueur("Audrey", "Vuillemin", Niveau.getNiveau("P12"), Joueur.Sexe.FEMME);

        currentTournoi = new Tournoi("Test", Tournoi.TypeTournoi.SIMPLE, 5, 5);
        currentTournoi.ajouteJoueur(john);
        currentTournoi.ajouteJoueur(olivier);
        currentTournoi.ajouteJoueur(stephane);
        currentTournoi.ajouteJoueur(audrey);*/

        this.primaryStage = primaryStage;

        vueGeneral = new GeneralView(this);
        Parent root = vueGeneral;
        Scene scenePrincipal = new Scene(root, 700, 700);
        scenePrincipal.getStylesheets().add("view/Style/Style.css");
        primaryStage.setScene(scenePrincipal);

        primaryStage.getIcons().add(chargeImage("/img/main.png"));

        nouveauTournoi(false);
        primaryStage.setTitle(currentTournoi.getNomTournoi());

        vueGeneral.refreshTournoi();

        primaryStage.show();
    }

    public boolean isTournoiAEnregistrer() {
        return tournoiAEnregistrer;
    }

    public Tournoi getCurrentTournoi() {
        return currentTournoi;
    }

    public ImageView chargeImageView(String chemin){
        InputStream input = getClass().getResourceAsStream(chemin);
        Image image;
        ImageView imageView;
        try{
            image = new Image(input);
            imageView = new ImageView(image);
            input.close();
            return imageView;
        }catch (Exception e){
            System.out.println("Image introuvable");
            return null;
        }
    }

    public Image chargeImage(String chemin){
        InputStream input = getClass().getResourceAsStream(chemin);
        Image image;
        try{
            image = new Image(input);
            return image;
        }catch (Exception e){
            System.out.println("Image introuvable");
            return null;
        }
    }

    public void ouvrirTournoi() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un tournoi existant");
        fileChooser.setInitialDirectory(new File(getCheminTournois()));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            ouvreTournoi(file.getAbsolutePath());
        }
    }

    public String getCheminTournois(){

        File dossier = new File(getRepertoireCourant() + dossierRacine);
        if (!dossier.exists()){
            dossier.mkdirs();
        }
        /*File dossierImage = new File(getRepertoireCourant() + "/src/Photos");
        if (!dossierImage.exists()){
            dossierImage.mkdirs();
        }*/
        return dossier.getAbsolutePath();
    }

    public boolean tournoiExist(String file){
        File tournoi = new File(file);
        return tournoi.exists();
    }

    public String getRepertoireCourant(){
        return new java.io.File("").getAbsolutePath();
    }

    public boolean enregistreTournoi() {
        if (currentTournoi != null){
            if (!currentTournoi.getNomTournoi().equals("")) {
                return enregistreTournoi2(getCheminTournois(), currentTournoi.getNomTournoi());
            } else {
                updateInfo("Il faut mettre un nom au tournoi ");
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean enregistreTournoi2(String chemin, String nom) {
        try {
            FileOutputStream fileOut = new FileOutputStream(chemin + "/" + nom + ".trn");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(currentTournoi);
            out.close();
            fileOut.close();
            SimpleDateFormat d = new SimpleDateFormat ("dd/MM/yyyy" );
            SimpleDateFormat h = new SimpleDateFormat ("hh:mm");
            Date currentTime = new Date();
            updateEnregistrement("Enregistré à " + d.format(currentTime) + " - " + h.format(currentTime));
            return true;
        } catch (Exception i) {
            i.printStackTrace();
            updateInfo("Impossible de renommer le tournoi !");
            return false;
        }
    }

    public void ouvreTournoi(String chemin) {
        currentTournoi = null;
        try {
            FileInputStream fileIn = new FileInputStream(chemin);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            currentTournoi = (Tournoi) in.readObject();
            in.close();
            fileIn.close();
            updateInfo("Tournoi ouvert");
            vueGeneral.ouvreTournoi();
            primaryStage.setTitle(currentTournoi.getNomTournoi());
        }catch (Exception e){
            SimpleDateFormat d = new SimpleDateFormat ("dd_MM_yyyy" );
            SimpleDateFormat h = new SimpleDateFormat ("hh_mm_ss");
            Date currentTime = new Date();
            String nomTournoiOLD = "AncienTournoi-" + d.format(currentTime) + "-" + h.format(currentTime);
            copier(new File (chemin),new File(getCheminTournois()+ "/" + nomTournoiOLD + ".trn"));
            nouveauTournoi(true);
            updateInfo("Le tournoi existant n'était pas compatible !");
        }
    }

    //si le fichier existe demande pour écraser sinon on renomme
    public void renommerTournoi(){
        TextInputDialog dialog = new TextInputDialog(currentTournoi.getNomTournoi());
        dialog.setTitle("Nouveau nom de tournoi");
        dialog.setHeaderText("Vous allez renommer le tournoi");
        dialog.setContentText("Entrez le nouveau nom ici :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if (tournoiExist(getCheminTournois() + "/" + result.get() + ".trn")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Fichier existant");
                alert.setHeaderText("Le fichier " + result.get() + " existe déjà.");
                alert.setContentText("Voulez-vous l'écraser ?");

                Optional<ButtonType> result2 = alert.showAndWait();
                if (result2.get() == ButtonType.OK){
                    currentTournoi.setNomTournoi(result.get());
                    enregistreTournoi();
                } else {
                    updateInfo("Renommage annulé");
                }
            }else{
                renommerTournoi2(getCheminTournois()+ "/" + currentTournoi.getNomTournoi() + ".trn", getCheminTournois()+ "/" + result.get() + ".trn", result.get());
                String save = currentTournoi.getNomTournoi();
                currentTournoi.setNomTournoi(result.get());
                if (enregistreTournoi()){
                    primaryStage.setTitle(currentTournoi.getNomTournoi());
                }else{
                    currentTournoi.setNomTournoi(save);
                }
            }

        }
    }

    public boolean renommerTournoi2(String cheminAncien, String cheminNouveau, String nomTournoi){
        File source = new File(cheminAncien);
        File destination = new File(cheminNouveau);
        try{
            source.renameTo(destination);
            return true;
        }catch (Exception e){
            updateInfo("Impossible de mettre ce nom pour le tournoi");
            return false;
        }
    }

    public static boolean copier(File source, File dest) {
        try (InputStream sourceFile = new java.io.FileInputStream(source);
             OutputStream destinationFile = new FileOutputStream(dest)) {
            // Lecture par segment de 0.5Mo
            byte buffer[] = new byte[512 * 1024];
            int nbLecture;
            while ((nbLecture = sourceFile.read(buffer)) != -1){
                destinationFile.write(buffer, 0, nbLecture);
            }
        } catch (IOException e){
            e.printStackTrace();
            return false; // Erreur
        }
        return true; // Résultat OK
    }

    public void updateInfo(String txt) {
        vueGeneral.getStatusBar().updateInfo(txt);
    }

    public void updateEnregistrement(String txt) {
        vueGeneral.getStatusBar().updateEnregistrement(txt);
    }

    public ArrayList<Joueur> getListJoueurs () {
        if (currentTournoi != null)
            return currentTournoi.getListJoueurs();
        else return null;
    }

    public void modeEditionJoueurChange(boolean modeEdition) {
        if (!modeEdition)
            currentTournoi.enregistreListJoueur(vueGeneral.getListJoueurs());
        vueGeneral.refreshJoueurView();
        vueGeneral.refreshConfigView();
        saveAutomatique();
    }

    public void modeEditionConfigChange(boolean modeEdition) {
        if (!modeEdition)
            currentTournoi.enregistreConfig(vueGeneral.getTournoiConfig());
        vueGeneral.refreshTournoi();
        saveAutomatique();
    }

    public void abortJoueurChange() {
        vueGeneral.refreshJoueurView();
    }

    public void supprimerJoueur(String index) {
        Integer ind = Integer.valueOf(index);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression d'un joueur");
        alert.setHeaderText("Le joueur " + currentTournoi.getListJoueurs().get(ind) + " va être supprimé.");
        alert.setContentText("Etes vous sûr dez vouloir le faire ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            currentTournoi.supprimerJoueur(currentTournoi.getJoueur(ind));
            vueGeneral.refreshJoueurView();
            updateInfo("Joueur supprimé !");
        }
        saveAutomatique();
    }

    public void saveAutomatique(){
        if (saveAuto){
            enregistreTournoi();
            tournoiAEnregistrer = false;
        }else{
            tournoiAEnregistrer = true;
        }
    }

    public void clickSexe() {
        currentTournoi.enregistreListJoueur(vueGeneral.getListJoueurs());
        vueGeneral.refreshJoueurView();
    }

    public void ajouterJoueur() {
        Joueur j = new Joueur("Joueur" + (currentTournoi.getListJoueurs().size() + 1), "", Niveau.getNiveau("P12"), Joueur.Sexe.HOMME, true);
        if (currentTournoi.ajouteJoueur(j)){
            updateInfo("Vous pouvez renommer le joueur ajouté...");
        }else{
            updateInfo("Attention ce joueur existe déjà !");
        }
        vueGeneral.refreshJoueurView();
    }

    public void saveAuto(boolean selected) {
        this.saveAuto = selected;
        vueGeneral.refreshMainToolbar();
    }

    public void supprimerTout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de tous les joueurs");
        alert.setHeaderText("Tous les joueurs vont être supprimés.");
        alert.setContentText("Etes vous sûr dez vouloir le faire ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            currentTournoi.supprimerTout();
            vueGeneral.refreshJoueurView();
            updateInfo("Joueur supprimé !");
        }
        saveAutomatique();
    }

    //si nouveau existe alors on l'ouvre sinon on le créé
    public void nouveauTournoi(boolean force) {
        if (tournoiExist(getCheminTournois() + "/" + nomNouveauTournoi) && !force){
            ouvreTournoi(getCheminTournois() + "/" + nomNouveauTournoi);
            updateInfo(nomNouveauTournoi + " : existait déjà, il a donc été ouvert.");
        }else{
            currentTournoi = new Tournoi("Nouveau tournoi", Tournoi.TypeTournoi.SIMPLE, 5, 5);
            try{
                enregistreTournoi();
                primaryStage.setTitle(currentTournoi.getNomTournoi());
                vueGeneral.ouvreTournoi();
            }catch( Exception e){
                updateInfo("Problème pour enregistrer Nouveau tournoi.trn");
                currentTournoi = null;
            }

        }
    }

    public void choisirPhoto(String index) {
        Integer ind = Integer.valueOf(index);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.setInitialDirectory(new File(getRepertoireCourant() + "/src/Photos/"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            //copie
            copier(new File(file.getAbsolutePath()), new File(getRepertoireCourant() + "/src/Photos/" + file.getName()));
            currentTournoi.updatePhoto(ind, "/Photos/" + file.getName());
            vueGeneral.refreshJoueurView();
        }
    }

    public ImageView chargePhoto(Joueur j) {
        String chemin;
        ImageView img = null;
        if (j.getPhoto() != null){
            chemin =j.getPhoto();
            img = chargeImageView(chemin);
            if (img == null){
                img = chargePhotoDefaut(j);
            }
        }else{
            img = chargePhotoDefaut(j);
        }
        if (img != null){
            img.setFitHeight(30.0);
            img.setFitWidth(30.0);
        }
        return img;
    }

    public ImageView chargePhotoDefaut(Joueur j){
        String chemin;
        ImageView img = null;
        if (img == null){
            if (j.getSexe() == Joueur.Sexe.HOMME){
                chemin = "/img/user-male-black-shape.png";
            }else{
                chemin = "/img/woman.png";
            }
            img = chargeImageView(chemin);
        }
        return img;
    }

    public void genererTournoi() {
        currentTournoi.setCurrentTour(null);
        if (currentTournoi.generer()){
            updateInfo("Tournoi généré");
            vueGeneral.refreshTournoi();
            vueGeneral.selectTabTournoi();
        }else{
            updateInfo("Nombre de joueurs insuffisant pour générer un tournoi !");
        }
    }

    public String getTailleTexte() {
        return "1.2em";
    }

    public void monterContrainte(String id) {
        currentTournoi.monterContrainte(id);
        vueGeneral.refreshTournoi();
    }

    public void descendreContrainte(String id) {
        currentTournoi.descendreContrainte(id);
        vueGeneral.refreshTournoi();
    }

    public void selectionJoueur(String id, boolean actif) {
        if (!tournoiEnCours()){
            Integer ind = Integer.valueOf(id);
            currentTournoi.enregistreSelection(ind, actif);
            vueGeneral.refreshTournoi();
        }else{
            updateInfo("Un tournoi est en cours, veuillez arrêter le tournoi avant de sélectionner des joueurs !");
        }
        saveAutomatique();
    }

    public boolean tourIsCloture(Tour tour) {
        return tour.getNumeroTour() < currentTournoi.getCurrentTour();
    }

    public void clotureTour() {
        currentTournoi.clotureTour();
        vueGeneral.refreshTournoi();
        vueGeneral.selectCurrentTabTour();
        saveAutomatique();
    }

    public void redistribuerJoueur() {
        currentTournoi.generer();
        vueGeneral.refreshTournoi();
    }

    public void scoreChange(Integer numeroTour, Integer numeroMatch, Integer newValue, boolean equipeA) {
        currentTournoi.changeScore(numeroTour, numeroMatch, newValue, equipeA);
        currentTournoi.trier();
        vueGeneral.refreshTournoiToolbar();
        vueGeneral.refreshJoueurView();
        saveAutomatique();
    }

    public void arreterTournoi() {
        currentTournoi.setTournoiEnCours(false);
        vueGeneral.refreshTournoi();
        saveAutomatique();
    }

    public boolean tournoiEnCours() {
        if (getCurrentTournoi() != null)
            return getCurrentTournoi().isTournoiEnCours();
        else
            return false;
    }

    public boolean getNbreJoueurSuffisant() {
        if (currentTournoi != null){
            return currentTournoi.getNbreJoueurSuffisant();
        }else{
            return false;
        }
    }

    public boolean isJoueurModeEdition() {
        if(vueGeneral != null){
            return vueGeneral.getVueJoueur().isModeEdition();
        }else{
            return false;
        }
    }

    public Integer getCurrentTour() {
        if (getCurrentTournoi() != null){
            return getCurrentTournoi().getCurrentTour();
        }else{
            return null;
        }
    }

    public void changeContrainteShow() {
        Integer numTourAfficher = vueGeneral.getNumeroTourAffiche();
        currentTournoi.changeContrainteShow(vueGeneral.getContrainteShow());
        saveAutomatique();
        vueGeneral.refreshTournoiView();
        vueGeneral.afficheTabTour(numTourAfficher);
    }

}
