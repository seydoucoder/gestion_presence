package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.gestion_presence.gestion_presence.Dao.UserDao;
import com.gestion_presence.gestion_presence.Models.User;

public class LoginController implements Initializable {

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnLogin;


    @FXML
    void connection(ActionEvent event) {
        if (validateInputs()) {
            String email = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            UserDao userDao = new UserDao();
            User user = userDao.getUserByEmailAndPassword(email, password);

            if (user != null) {

                SessionManager.getInstance().setUser(user);

                System.out.println("Utilisateur connecté : " + user.getEmail() + " - Rôle : " + user.getRole().getClass());


                redirigerApresConnexion();
            } else {
                showError("Erreur de connexion", "Email ou mot de passe incorrect.");
            }
        }
    }




    private boolean validateInputs() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            showError("Nom d'utilisateur ou mot de passe vide", "Veuillez remplir tous les champs.");
            return false;
        }

        return true;
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void redirigerApresConnexion() {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestion_presence/gestion_presence/gestion-presence.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Gestion de Présence");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(this::connection);
    }
}
