package com.youcode.covoiturageapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conducteurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conducteur extends Personne{
    @Column(name = "numero_permis", unique = true , nullable = false)
    private String numeroPermis;
}
