package com.youcode.covoiturageapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "points_arret")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PointArret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ville;

    @Column(name = "ordre", nullable = false)
    private Integer ordre;

    @Column(name = "heure_passage", nullable = false)
    private LocalTime heurePassage;

    @Column( name = "prix_depuis_depart", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixDepuisDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "trajet_id" , nullable = false)
    private Trajet trajet;
    
}
