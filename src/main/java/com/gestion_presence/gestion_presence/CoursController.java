package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.CoursDao;
import com.gestion_presence.gestion_presence.Dao.SalleDao;
import com.gestion_presence.gestion_presence.Dao.UserDao;
import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.Models.Salle;
import com.gestion_presence.gestion_presence.Models.User;
import com.gestion_presence.gestion_presence.utils.EmailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class CoursController implements Initializable {

    @FXML private TableView<Cours> tableCours;
    @FXML private TableColumn<Cours, Integer> colId;
    @FXML private TableColumn<Cours, String> colNom;
    @FXML private TableColumn<Cours, String> colDescription;
    @FXML private TableColumn<Cours, String> colHeureDebut;
    @FXML private TableColumn<Cours, String> colHeureFin;
    @FXML private TableColumn<Cours, String> colSalle;
    @FXML
    private TableColumn<Cours, String> colJour;
    @FXML
    private TableColumn<Cours, String> colProf;

    @FXML private TextField txtNom;
    @FXML private TextField txtDescription;
    @FXML private ComboBox<User> comboProfesseur;
    @FXML
    private ComboBox<Salle> cmbSalle;
    @FXML
    private ComboBox<String> cmbJour;

    @FXML private ComboBox<LocalTime> cmbHeureDebut;
    @FXML private ComboBox<LocalTime> cmbHeureFin;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;


    private final CoursDao coursDao = new CoursDao();
    private final SalleDao salleDao = new SalleDao();
    UserDao userDao = new UserDao();


    private void chargerProfs() {
        List<User> profs = userDao.getUsersByProfs();
        ObservableList<User> profList = FXCollections.observableArrayList(profs);
        comboProfesseur.setItems(profList);

    }

    private void chargerSalles() {
        List<Salle> salles = salleDao.getAllSalles();
        ObservableList<Salle> sallesList = FXCollections.observableArrayList(salles);
        cmbSalle.setItems(sallesList);


    }

    private void chargerHeures() {

        var heures = FXCollections.observableArrayList(
                IntStream.range(7, 21)
                        .mapToObj(h -> LocalTime.of(h, 0))
                        .toList()
        );

        cmbHeureDebut.setItems(heures);
        cmbHeureFin.setItems(heures);
    }

    @FXML
    public void addCours() {
        if (cmbHeureDebut.getValue().isAfter(cmbHeureFin.getValue()) || cmbHeureDebut.getValue().equals(cmbHeureFin.getValue())) {
            afficherAlerte("Erreur", "L'heure de début doit être avant l'heure de fin.");
            return;
        }

        if (txtNom.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                cmbHeureDebut.getValue() == null || cmbHeureFin.getValue() == null || cmbSalle.getValue() == null || cmbJour.getValue() == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (coursDao.existeCours(txtNom.getText())) {
            afficherAlerte("Erreur", "Un cours avec ce nom existe déjà.");
            return;
        }
        int salleId = cmbSalle.getValue().getId();
        if (coursDao.existeChevauchement(salleId, cmbHeureDebut.getValue(), cmbHeureFin.getValue(), cmbJour.getValue())) {
            afficherAlerte("Erreur", "Un autre cours est déjà prévu dans cette salle sur ce créneau horaire.");
            return;
        }

        Cours newCours = Cours.builder()
                .nom(txtNom.getText().substring(0, 1).toUpperCase() + txtNom.getText().substring(1).toLowerCase())
                .description(txtDescription.getText())
                .heureDebut(cmbHeureDebut.getValue())
                .heureFin(cmbHeureFin.getValue())
                .salle(cmbSalle.getValue())
                .jour(cmbJour.getValue())
                .professeur(comboProfesseur.getValue())
                .build();

        coursDao.addCours(newCours);

        String emailProfesseur = newCours.getProfesseur().getEmail();
        String sujet = "Nouveau cours ajouté : " + newCours.getNom();
        String message = "Bonjour " + newCours.getProfesseur().getNom() + ",\n\n" +
                "Un nouveau cours a été ajouté : " + newCours.getNom() + ".\n" +
                "Description : " + newCours.getDescription() + "\n" +
                "Date : " + newCours.getJour() + "\n" +
                "Horaire : " + newCours.getHeureDebut() + " - " + newCours.getHeureFin() + "\n" +
                "Salle : " + newCours.getSalle().getLibelle() + "\n\n" +
                "Merci de votre engagement.\nCordialement.";

        EmailService.envoyerEmail(emailProfesseur, sujet, message);
        afficherAlerte("Succès", "Le cours a été ajouté avec succès.");
        loadTableData();
        txtNom.clear();
        txtDescription.clear();
        cmbHeureDebut.setValue(null);
        cmbHeureFin.setValue(null);
        cmbSalle.setValue(null);
        cmbJour.setValue(null);
        comboProfesseur.setValue(null);
    }

    @FXML
    void deleteCours(ActionEvent event) {
        Cours selectedCours = tableCours.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            coursDao.deleteCours(selectedCours.getId());
           loadTableData();
            afficherAlerte("Succès", "Le cours a été supprimé avec succès.");
        } else {
            afficherAlerte("Erreur", "Sélectionnez un cours à supprimer.");
        }
        txtNom.clear();
        txtDescription.clear();
        cmbHeureDebut.setValue(null);
        cmbHeureFin.setValue(null);
        cmbSalle.setValue(null);
        cmbJour.setValue(null);
        comboProfesseur.setValue(null);
    }

    @FXML
    void modifCours(ActionEvent event) {
        Cours selectedCours = tableCours.getSelectionModel().getSelectedItem();
        if (selectedCours == null) {
            afficherAlerte("Erreur", "Sélectionnez un cours à modifier.");
            return;
        }
        if (cmbHeureDebut.getValue().isAfter(cmbHeureFin.getValue()) || cmbHeureDebut.getValue().equals(cmbHeureFin.getValue())) {
            afficherAlerte("Erreur", "L'heure de début doit être avant l'heure de fin.");
            return;
        }

        int salleId = cmbSalle.getValue().getId();
        String jour = cmbJour.getValue();
        LocalTime heureDebut = cmbHeureDebut.getValue();
        LocalTime heureFin = cmbHeureFin.getValue();


        if (coursDao.existeChevauchementModification(selectedCours.getId(), salleId, heureDebut, heureFin, jour)) {
            afficherAlerte("Erreur", "Un autre cours est déjà prévu dans cette salle sur ce créneau horaire.");
            return;
        }
        selectedCours.setNom(txtNom.getText().substring(0, 1).toUpperCase() + txtNom.getText().substring(1).toLowerCase());
        selectedCours.setDescription(txtDescription.getText());
        selectedCours.setHeureDebut(cmbHeureDebut.getValue());
        selectedCours.setHeureFin(cmbHeureFin.getValue());
        selectedCours.setSalle(cmbSalle.getValue());
        selectedCours.setJour(cmbJour.getValue());
        selectedCours.setProfesseur(comboProfesseur.getValue());

        coursDao.updateCours(selectedCours);
        String emailProfesseur = selectedCours.getProfesseur().getEmail();
        String sujet = "Modification de votre cours : " + selectedCours.getNom();
        String message = "Bonjour " + selectedCours.getProfesseur().getNom() + ",\n\n" +
                "Votre cours a été modifié.\n\n" +
                "Nouveau nom : " + selectedCours.getNom() + "\n" +
                "Description : " + selectedCours.getDescription() + "\n" +
                "Jour : " + selectedCours.getJour() + "\n" +
                "Nouvel horaire : " + selectedCours.getHeureDebut() + " - " + selectedCours.getHeureFin() + "\n" +
                "Salle : " + selectedCours.getSalle().getLibelle() + "\n\n" +
                "Merci de votre engagement.\nCordialement.";

        EmailService.envoyerEmail(emailProfesseur, sujet, message);
        afficherAlerte("Succès", "Le cours a été modifié avec succès.");
        loadTableData();

        txtNom.clear();
        txtDescription.clear();
        cmbHeureDebut.setValue(null);
        cmbHeureFin.setValue(null);
        cmbSalle.setValue(null);
        cmbJour.setValue(null);
        comboProfesseur.setValue(null);
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadTableData() {
        List<Cours> cours = coursDao.getAllCours();
        ObservableList<Cours> CoursList = FXCollections.observableArrayList(cours);
        tableCours.setItems(CoursList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        colJour.setCellValueFactory(new PropertyValueFactory<>("jour"));
        colProf.setCellValueFactory(new PropertyValueFactory<>("professeur"));

        tableCours.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNom.setText(newValue.getNom());
                txtDescription.setText(newValue.getDescription());
                cmbHeureDebut.setValue(newValue.getHeureDebut());
                cmbHeureFin.setValue(newValue.getHeureFin());
                cmbSalle.setValue(newValue.getSalle());
                comboProfesseur.setValue(newValue.getProfesseur());
                cmbJour.setValue(newValue.getJour());
                btnAjouter.setDisable(true);
                btnModifier.setDisable(false);
                btnSupprimer.setDisable(false);
            }else{
                btnAjouter.setDisable(false);
                btnSupprimer.setDisable(true);
                btnModifier.setDisable(true);
            }
        });
        cmbJour.getItems().addAll("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche");
        chargerSalles();
        loadTableData();
        chargerHeures();
        chargerProfs();
    }
}
