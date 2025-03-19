package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.SalleDao;
import com.gestion_presence.gestion_presence.Models.Salle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalleController implements Initializable {

    @FXML private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, Integer> colId;
    @FXML private TableColumn<Salle, String> colLibelle;

    @FXML private TextField txtLibelle;

    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    private final SalleDao salleDao = new SalleDao();

    @FXML
    void addSalle(ActionEvent event) {
        String libelle = txtLibelle.getText().substring(0, 1).toUpperCase() + txtLibelle.getText().substring(1).toLowerCase();
        if (!libelle.isEmpty()) {

            if (salleDao.existeSalleParLibelle(libelle)) {
                afficherAlerte("Erreur", "Une salle avec ce nom existe déjà.");
            } else {

                Salle salle = Salle.builder()
                        .libelle(libelle)
                        .build();
                salleDao.addSalle(salle);
                chargerSalles();
                afficherAlerte("Succès", "Salle ajoutée avec succès.");
                txtLibelle.clear();
            }
        } else {
            afficherAlerte("Erreur", "Le libellé de la salle ne peut pas être vide.");
        }
    }


    @FXML
    void modifSalle(ActionEvent event) {
        Salle selectedSalle = tableSalles.getSelectionModel().getSelectedItem();
        if (selectedSalle != null) {
            String newLibelle = txtLibelle.getText();
            if (!newLibelle.isEmpty()) {
                selectedSalle.setLibelle(newLibelle.substring(0, 1).toUpperCase() + newLibelle.substring(1).toLowerCase());
                boolean success = salleDao.updateSalle(selectedSalle);
                if (success) {
                    loadTableData();
                    showAlert("Succès", "Salle mise à jour avec succès.");
                } else {
                    showAlert("Erreur", "La mise à jour a échoué.");
                }
            } else {
                showAlert("Erreur", "Le libellé de la salle ne peut pas être vide.");
            }
        }

    }


    @FXML
    void deleteSalle(ActionEvent event) {
        Salle selectedSalle = tableSalles.getSelectionModel().getSelectedItem();
        if (selectedSalle != null) {
            boolean success = salleDao.deleteSalle(selectedSalle.getId());
            if (success) {
                loadTableData();
                txtLibelle.clear();
                showAlert("Succès", "Salle supprimée avec succès.");
            } else {
                showAlert("Erreur", "La suppression a échoué.");
            }
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void chargerSalles() {
        ObservableList<Salle> salles = FXCollections.observableArrayList(salleDao.getAllSalles());
        tableSalles.setItems(salles);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadTableData() {
        ObservableList<Salle> salles = FXCollections.observableArrayList(salleDao.getAllSalles());
        tableSalles.setItems(salles);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        btnSupprimer.setDisable(true);
        btnModifier.setDisable(true);

        chargerSalles();

        tableSalles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtLibelle.setText(newValue.getLibelle());
                btnAjouter.setDisable(true);
                btnSupprimer.setDisable(false);
                btnModifier.setDisable(false);
            } else {
                btnAjouter.setDisable(false);
                btnSupprimer.setDisable(true);
                btnModifier.setDisable(true);
            }
        });
    }
}
