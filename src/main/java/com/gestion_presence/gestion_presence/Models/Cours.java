package com.gestion_presence.gestion_presence.Models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "cours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String description;

    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String jour;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;
    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private User professeur;



    public Cours(String nom, String description, LocalTime heureDebut, LocalTime heureFin,User professeur, Salle salle, String jour) {
        this.nom = nom;
        this.description = description;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.salle = salle;
        this.jour = jour;
        this.professeur = professeur;
    }

    @Override
    public String toString() {
        return nom;
    }


}
