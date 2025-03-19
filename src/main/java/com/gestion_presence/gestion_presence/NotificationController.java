package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Models.Notifications;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;

public class NotificationController {

    @FXML private TableView<Notifications> tableNotifications;
    @FXML private TableColumn<Notifications, Integer> colId;
    @FXML private TableColumn<Notifications, String> colMessage;
    @FXML private TableColumn<Notifications, String> colDestinataire;
    @FXML private TableColumn<Notifications, LocalDate> colDateEnvoi;

    @FXML private TextArea txtMessage;
    @FXML private ComboBox<String> comboDestinataire;

    @FXML private Button btnAjouter;


    @FXML
    public void envoyerNotification() {

    }
}
