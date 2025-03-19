package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.RoleDao;

import com.gestion_presence.gestion_presence.Dao.UserDao;

import com.gestion_presence.gestion_presence.Models.Cours;
import com.gestion_presence.gestion_presence.Models.Notifications;
import com.gestion_presence.gestion_presence.Models.Role;

import com.gestion_presence.gestion_presence.Models.User;
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

public class UsersController implements Initializable {

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNom;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPrenom;
    @FXML private ComboBox<Role> comboRole;

    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private final UserDao userDao = new UserDao();
    private final RoleDao roleDao = new RoleDao();

    private void chargerRoles() {
        List<Role> roles = roleDao.getAllRole();
        ObservableList<Role> roleList = FXCollections.observableArrayList(roles);
        comboRole.setItems(roleList);


    }


    @FXML
    void addUser(ActionEvent event) {

        User newUser  = User.builder()
                .nom(txtNom.getText().substring(0, 1).toUpperCase() + txtNom.getText().substring(1).toLowerCase())
                .prenom(txtPrenom.getText().substring(0, 1).toUpperCase() + txtPrenom.getText().substring(1).toLowerCase())
                .email(txtEmail.getText())
                .password(txtPassword.getText())
                .role(comboRole.getValue())
                .build();
        userDao.addUser(newUser);
        loadTableData();
        afficherAlerte("Succès", "Users ajouté avec succès.");
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtPassword.clear();
        comboRole.setValue(null);
    }

    @FXML
    void deleteUser(ActionEvent event) {
        User selectUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectUser != null) {
            userDao.deleteUser(selectUser.getId());
            loadTableData();
            afficherAlerte("Succès", "Users supprimé");
        } else {
            afficherAlerte("Erreur", "Sélectionnez un cours à supprimer.");
        }
    }

    @FXML
    void modifyUser(ActionEvent event) {
        User selectUser = tableUsers.getSelectionModel().getSelectedItem();
        if (selectUser == null) {
            afficherAlerte("Erreur", "Sélectionnez un user à modifier.");
            return;
        }



        selectUser.setNom(txtNom.getText().substring(0, 1).toUpperCase() + txtNom.getText().substring(1).toLowerCase());
        selectUser.setPrenom(txtPrenom.getText().substring(0, 1).toUpperCase() + txtPrenom.getText().substring(1).toLowerCase());
        selectUser.setEmail(txtEmail.getText());

        selectUser.setRole(comboRole.getValue());

        userDao.updateUser(selectUser);
        loadTableData();
        afficherAlerte("Succès", "Users modifie avec succès.");
        txtPrenom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        comboRole.setValue(null);
    }

    private void loadTableData() {
        List<User> users = userDao.getAllUsers();
        ObservableList<User> UsersList = FXCollections.observableArrayList(users);
        tableUsers.setItems(UsersList);
    }
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNom.setText(newValue.getNom());
                txtPrenom.setText(newValue.getPrenom());
                txtEmail.setText(newValue.getEmail());

                comboRole.setValue(newValue.getRole());
                btnAjouter.setDisable(true);
                btnModifier.setDisable(false);
                btnSupprimer.setDisable(false);
            }else{
                btnAjouter.setDisable(false);
                btnSupprimer.setDisable(true);
                btnModifier.setDisable(true);
            }
        });

        chargerRoles();
        loadTableData();
    }
}
