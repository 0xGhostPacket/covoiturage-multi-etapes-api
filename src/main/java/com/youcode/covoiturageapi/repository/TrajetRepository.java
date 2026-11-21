package com.youcode.covoiturageapi.repository;

import com.youcode.covoiturageapi.entity.Trajet;
import com.youcode.covoiturageapi.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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


    // 3
    @Query("SELECT DISTINCT t FROM Trajet t " +
            "JOIN t.pointsArret pDepart " +
            "JOIN t.pointsArret pArrivee " +
            "WHERE LOWER(pDepart.ville) = LOWER(:villeDepart) " +
            "AND LOWER(pArrivee.ville) = LOWER(:villeArrivee) " +
            "AND pDepart.ordre < pArrivee.ordre " +
            "AND t.statut = :statut " +
            "AND t.dateDepart >= :debutJournee AND t.dateDepart < :finJournee")
    Page<Trajet> searchTrajets(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("statut") StatutTrajet statut,
            @Param("debutJournee") LocalDateTime debutJournee,
            @Param("finJournee") LocalDateTime finJournee,
            Pageable pageable
    );
}