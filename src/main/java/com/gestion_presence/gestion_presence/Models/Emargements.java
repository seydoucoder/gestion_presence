package com.gestion_presence.gestion_presence.Models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "emargements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Emargements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private User professeur;

    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours cours;



    public Emargements(Date date, String statut, User professeur, Cours cours) {
        this.date = date;
        this.statut = statut;
        this.professeur = professeur;
        this.cours = cours;
    }


}
