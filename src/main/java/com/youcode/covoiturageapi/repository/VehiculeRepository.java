package com.youcode.covoiturageapi.repository;

import com.youcode.covoiturageapi.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {

    // check if exist
    boolean existsByImmatriculation(String immatriculation);
    // check if exist but no ralated to the same id (check duplicate)
    boolean existsByImmatriculationAndIdNot(String immatriculation, Long id);
    // list all Vehcule related to a conducteur
    List<Vehicule> findByConducteurId(Long conducteurId);
}
