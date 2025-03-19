package com.gestion_presence.gestion_presence.Models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnvoi;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;

    public Notifications() {
    }

    public Notifications(String message, Date dateEnvoi, User destinataire) {
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.destinataire = destinataire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public User getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }
}
