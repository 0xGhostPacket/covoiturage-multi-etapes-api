package com.youcode.covoiturageapi.repository;

import com.youcode.covoiturageapi.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    // 1. Fetch trip details with stops in ONE query (Prevents N+1 problem!)
    @Query("SELECT t FROM Trajet t " +
            "LEFT JOIN FETCH t.pointsArret " +
            "LEFT JOIN FETCH t.conducteur " +
            "LEFT JOIN FETCH t.vehicule " +
            "WHERE t.id = :id")
    Optional<Trajet> findByIdWithDetails(@Param("id") Long id);

    // 2. Check if a vehicle is used in any trip (for vehicle deletion rule!)
    boolean existsByVehiculeId(Long vehiculeId);
}