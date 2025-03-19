package com.gestion_presence.gestion_presence;



import com.gestion_presence.gestion_presence.Dao.EmargementsDao;
import com.gestion_presence.gestion_presence.Dao.CoursDao;
import com.gestion_presence.gestion_presence.Dao.SessionManager;
import com.gestion_presence.gestion_presence.Models.Cours;

import com.gestion_presence.gestion_presence.Models.Emargements;

import com.gestion_presence.gestion_presence.Models.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ProfesseurController {

    @FXML
    private TableView<Cours> tableCours;

    @FXML
    private TableColumn<Cours, String> colNomCours;

    @FXML
    private TableColumn<Cours, String> colHeureDebut;

    @FXML
    private TableColumn<Cours, String> colHeureFin;

    @FXML
    private TableColumn<Cours, String> colJour;
    @FXML
    private Button btnEmarger;

    private ObservableList<Cours> coursList = FXCollections.observableArrayList();
    private CoursDao coursDao = new CoursDao();
    private EmargementsDao emargementDao = new EmargementsDao();



    private void loadCoursProfesseur() {
        User professeur = SessionManager.getInstance().getUser();
        if (professeur != null) {
            List<Cours> coursProfesseur = coursDao.getCoursByProfesseur(professeur.getId());

            coursList.setAll(coursProfesseur);
            tableCours.setItems(coursList);
        }
    }

    private String recupDate(String jours) {
        switch (jours) {
            case "MONDAY":
                return "Lundi";
            case "TUESDAY":
                return "Mardi";
            case "WEDNESDAY":
                return "Mercredi";
            case "THURSDAY":
                return "Jeudi";
            case "FRIDAY":
                return "Vendredi";
            case "SATURDAY":
                return "Samedi";
            case "SUNDAY":
                return "Dimanche";
            default:
                return "";
        }
    }

    private void enregistrerPresence() {
        Cours selectedCours = tableCours.getSelectionModel().getSelectedItem();
        if (selectedCours == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un cours avant d'émarger.");
            return;
        }

        User professeur = SessionManager.getInstance().getUser();
        if (professeur == null) {
            showAlert("Erreur", "Aucun professeur connecté !");
            return;
        }


        Emargements existingEmargement = emargementDao.getEmargementProf(selectedCours, professeur, Date.valueOf(LocalDate.now()));
        if (existingEmargement != null) {
            showAlert("Emargement déjà effectué", "Vous avez déjà émargé pour ce cours aujourd'hui.");
            return;
        }


        String jourCours = selectedCours.getJour();
        String jourActuel = recupDate(LocalDate.now().getDayOfWeek().toString());

        if (!jourCours.equalsIgnoreCase(jourActuel)) {
            showAlert("Erreur", "C'est pas le jour");
            return;
        }


        try {
            LocalTime now = LocalTime.now();
            LocalTime heureDebut = selectedCours.getHeureDebut();
            LocalTime heureFin = selectedCours.getHeureFin();
            LocalTime limiteEmargement = heureDebut.plusMinutes(15);
//            if (now.isBefore(heureDebut) || now.isAfter(heureFin)) {
//                showAlert("Emargement refusé", "Vous ne pouvez émarger qu'entre " + heureDebut + " et " + heureFin + ".");
//                return;
//            }

            if (now.isAfter(limiteEmargement)) {
                showAlert("Emargement refusé", "Vous avez dépassé le délai d’émargement de 15 minutes !");
                return;
            }
            Emargements emargement = new Emargements();
            emargement.setProfesseur(professeur);
            emargement.setCours(selectedCours);
            emargement.setStatut("Present");
            emargement.setDate(Date.valueOf(LocalDate.now()));

            emargementDao.addEmargement(emargement);
            showAlert("Succès", "Présence enregistrée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'émargement.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        colNomCours.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colHeureDebut.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHeureDebut().toString()));
        colHeureFin.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHeureFin().toString()));
        colJour.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getJour()));


        loadCoursProfesseur();

        btnEmarger.setOnAction(e -> enregistrerPresence());
    }
}
