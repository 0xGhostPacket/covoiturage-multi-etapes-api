package com.youcode.covoiturageapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "passagers")
@Getter
@Setter
@NoArgsConstructor
public class Passager extends Personne {

}
