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
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.net.URL;
import java.time.LocalDate;

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

    UserDao userDao = new UserDao();
    CoursDao coursDao = new CoursDao();
    EmargementsDao emargementsDao = new EmargementsDao();
    private ExportService exportService = new ExportService();

//    public void handleExportPDF() {
//        // Récupérer les émargements de la base de données
//        List<Emargements> emargements = emargementsDao.getAllEmargements();
//
//        // Définir le chemin du fichier
//        String filePath = "export_emargements.pdf";
//
//        // Exportation en PDF
//        exportService.exportToPDF(emargements, filePath);
//    }
    @FXML
    void handleExportPDF(ActionEvent event) {
        // Ouvrir une boîte de dialogue pour choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

        // Obtenir la fenêtre actuelle
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Récupérer les émargements depuis la base de données
            List<Emargements> emargements = emargementsDao.getAllEmargements();

            // Appeler le service d'exportation
            exportService.exportToPDF(emargements, file.getAbsolutePath());
        }
    }
    @FXML
    void handleExportExcel(ActionEvent event) {
        // Ouvrir une boîte de dialogue pour choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        fileChooser.setTitle("Enregistrer le fichier Excel");

        // Obtenir la fenêtre actuelle
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Récupérer les émargements depuis la base de données
            List<Emargements> emargements = emargementsDao.getAllEmargements();

            // Appeler le service d'exportation en Excel
            exportService.exportToExcel(emargements, file.getAbsolutePath());
        }
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
