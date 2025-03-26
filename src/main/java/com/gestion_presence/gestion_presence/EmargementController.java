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
    private void chargerFiltrerParDate(ActionEvent event) {
        LocalDate dateDebut = datePickerDebut.getValue();
        LocalDate dateFin = datePickerFin.getValue();

        if (dateDebut != null && dateFin != null) {

            Date dateDebutJava = java.sql.Date.valueOf(dateDebut);
            Date dateFinJava = java.sql.Date.valueOf(dateFin);


            List<Emargements> emargementsFiltrés = emargementsDao.getEmargementsBetweenDates(dateDebutJava, dateFinJava);


            tableEmargements.getItems().setAll(emargementsFiltrés);
        }
    }


    EmargementsDao emargementsDao = new EmargementsDao();
    private ExportService exportService = new ExportService();

    @FXML
    void chargerExportPDF(ActionEvent event) {

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
    void chargerExportExcel(ActionEvent event) {

        List<Emargements> emargements = new ArrayList<>(tableEmargements.getItems());


        if (emargements.isEmpty()) {
            System.out.println("Aucune donnée à exporter.");
            return;
        }

        String filePath = "export_emargements.xlsx";


        exportService.exportToExcel(emargements, filePath);

        System.out.println("Exportation Excel réussie !");
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        btnFiltrerDates.setOnAction(event -> chargerFiltrerParDate(event));
        if (btnExportPDF != null) {
            btnExportPDF.setOnAction(this::chargerExportPDF);
        }
        if (btnExportExcel != null) {
            btnExportExcel.setOnAction(this::chargerExportExcel);
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
        colCours.setCellValueFactory(new PropertyValueFactory<>("cours"));


        loadTableData();

    }
}
