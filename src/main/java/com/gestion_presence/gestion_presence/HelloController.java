package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.SessionManager;
import com.gestion_presence.gestion_presence.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Button btnDash;
    @FXML
    private Button btnCours;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEmerge;



    @FXML
    private Button btnSalle;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnProf;

    @FXML
    private AnchorPane contentArea;
    private User currentUser;





    private void loadPage(String page) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
            Node newPage = loader.load();
            contentArea.getChildren().setAll(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadCurrentUser() {
        User currentUser = SessionManager.getInstance().getUser();

        if (currentUser != null) {
            System.out.println("Utilisateur connecté : " + currentUser.getEmail() + " - Rôle : " + currentUser.getRole());
            adjustUIForRole(String.valueOf(currentUser.getRole()));
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    private void adjustUIForRole(String roleName) {
        if (roleName.equals("Admin")) {
            btnUser.setVisible(true);
            btnProf.setVisible(false);
        } else if(roleName.equals("gestionnaires")) {
            btnUser.setVisible(false);
        }else{
            btnUser.setVisible(false);
            btnDash.setVisible(false);
            btnCours.setVisible(false);
            btnEmerge.setVisible(false);
            btnSalle.setVisible(false);

        }
    }


    @FXML
    private void logout(ActionEvent event) {
        SessionManager.getInstance().logout();

        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestion_presence/gestion_presence/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Connexion");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeButton) {

        btnDash.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");
        btnSalle.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");
        btnCours.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");
        btnEmerge.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");

        btnUser.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");
        btnProf.setStyle("-fx-background-color: #3f2b63; -fx-text-fill: white;");


        activeButton.setStyle("-fx-background-color: #5a3d91; -fx-text-fill: white;");
    }



    @FXML
    private void initialize() {

        loadCurrentUser();
        loadPage("fxml/dashboard.fxml");
        setActiveButton(btnDash);

        btnDash.setOnAction(e -> {
            loadPage("fxml/dashboard.fxml");
            setActiveButton(btnDash);
        });


        btnSalle.setOnAction(e -> {
            loadPage("fxml/salle.fxml");
            setActiveButton(btnSalle);
        });

        btnCours.setOnAction(e -> {
            loadPage("fxml/cours.fxml");
            setActiveButton(btnCours);
        });

        btnEmerge.setOnAction(e -> {
            loadPage("fxml/emargement.fxml");
            setActiveButton(btnEmerge);
        });



        btnUser.setOnAction(e -> {
            loadPage("fxml/users.fxml");
            setActiveButton(btnUser);
        });

        btnProf.setOnAction(e -> {
            loadPage("fxml/professeur.fxml");
            setActiveButton(btnDash);
        });
    }
}
