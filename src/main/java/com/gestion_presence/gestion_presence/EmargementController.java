package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.CoursDao;
import com.gestion_presence.gestion_presence.Dao.EmargementsDao;
import com.gestion_presence.gestion_presence.Dao.UserDao;
import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.Models.Emargements;
import com.gestion_presence.gestion_presence.utils.ExportService;
import javafx.event.ActionEvent;
import com.gestion_presence.gestion_presence.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmargementController implements Initializable {

    @FXML private TableView<Emargements> tableEmargements;
    @FXML private TableColumn<Emargements, Integer> colId;
    @FXML private TableColumn<Emargements, LocalDate> colDate;
    @FXML private TableColumn<Emargements, String> colStatut;
    @FXML private TableColumn<Emargements, String> colProfesseur;
    @FXML private TableColumn<Emargements, String> colCours;

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboStatut;
    @FXML private ComboBox<User> comboProfesseur;
    @FXML private ComboBox<Cours> comboCours;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML
    private Button btnExportPDF;
    @FXML
    private Button btnExportExcel;
    @FXML
    private DatePicker datePickerDebut;
    @FXML
    private Button btnFiltrerDates;
    @FXML
    private DatePicker datePickerFin;
    @FXML
    private void handleFiltrerParDate(ActionEvent event) {
        LocalDate dateDebut = datePickerDebut.getValue();
        LocalDate dateFin = datePickerFin.getValue();

        if (dateDebut != null && dateFin != null) {
            // Convertir LocalDate en Date (java.util.Date)
            Date dateDebutJava = java.sql.Date.valueOf(dateDebut);
            Date dateFinJava = java.sql.Date.valueOf(dateFin);

            // Récupérer les émargements filtrés
            List<Emargements> emargementsFiltrés = emargementsDao.getEmargementsBetweenDates(dateDebutJava, dateFinJava);

            // Mettre à jour la TableView avec les résultats filtrés
            tableEmargements.getItems().setAll(emargementsFiltrés);
        }
    }

    UserDao userDao = new UserDao();
    CoursDao coursDao = new CoursDao();
    EmargementsDao emargementsDao = new EmargementsDao();
    private ExportService exportService = new ExportService();

    @FXML
    void handleExportPDF(ActionEvent event) {

        List<Emargements> emargements = new ArrayList<>(tableEmargements.getItems());


        if (emargements.isEmpty()) {
            System.out.println("Aucune donnée à exporter.");
            return;
        }

        String filePath = "export_emargements.pdf";


        exportService.exportToPDF(emargements, filePath);

        System.out.println("Exportation PDF réussie !");
    }


    @FXML
    void handleExportExcel(ActionEvent event) {

        List<Emargements> emargements = new ArrayList<>(tableEmargements.getItems());


        if (emargements.isEmpty()) {
            System.out.println("Aucune donnée à exporter.");
            return;
        }

        String filePath = "export_emargements.xlsx";


        exportService.exportToExcel(emargements, filePath);

        System.out.println("Exportation Excel réussie !");
    }


    private void chargerProfs() {
        List<User> profs = userDao.getUsersByProfs();
        ObservableList<User> profList = FXCollections.observableArrayList(profs);
        comboProfesseur.setItems(profList);

    }
    private void chargerCours() {
        List<Cours> cours = coursDao.getAllCours();
        ObservableList<Cours> coursList = FXCollections.observableArrayList(cours);
        comboCours.setItems(coursList);
    }
    private void loadTableData() {
        List<Emargements> emarges = emargementsDao.getAllEmargements();
        ObservableList<Emargements> emargesList = FXCollections.observableArrayList(emarges);
        tableEmargements.setItems(emargesList);
    }
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void addEmarge(ActionEvent event) {
        Emargements newEmarge = Emargements.builder()
                .date(java.sql.Date.valueOf(datePicker.getValue()))
                .statut(comboStatut.getValue())
                .professeur(comboProfesseur.getValue())
                .cours(comboCours.getValue())
                .build();

        emargementsDao.addEmargement(newEmarge);
        loadTableData();
        afficherAlerte("Succès", "Emargement reussi");
        datePicker.setValue(null);
        comboStatut.setValue(null);
        comboProfesseur.setValue(null);
        comboCours.setValue(null);
    }

    @FXML
    void deleteEmarge(ActionEvent event) {
        Emargements selectedEmarge = tableEmargements.getSelectionModel().getSelectedItem();
        if (selectedEmarge != null) {
            emargementsDao.deleteEmargement(selectedEmarge.getId());
            loadTableData();
            afficherAlerte("Succès", "Emargement supprimé avec succès");
        } else {
            afficherAlerte("Erreur", "Veuillez sélectionner un émargement à supprimer");
        }
    }

    @FXML
    void modifEmarge(ActionEvent event) {

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        btnFiltrerDates.setOnAction(event -> handleFiltrerParDate(event));
        if (btnExportPDF != null) {
            btnExportPDF.setOnAction(this::handleExportPDF);
        }
        if (btnExportExcel != null) {
            btnExportExcel.setOnAction(this::handleExportExcel);
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
        tableEmargements.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                comboStatut.setValue(newValue.getStatut());
                comboProfesseur.setValue(newValue.getProfesseur());
                comboCours.setValue(newValue.getCours());

                btnAjouter.setDisable(true);
                btnModifier.setDisable(false);
                btnSupprimer.setDisable(false);
            }else{
                btnAjouter.setDisable(false);
                btnSupprimer.setDisable(true);
                btnModifier.setDisable(true);
            }
        });

        loadTableData();
        chargerCours();
        chargerProfs();
    }
}
