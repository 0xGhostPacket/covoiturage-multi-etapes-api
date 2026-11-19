package com.youcode.covoiturageapi.repository;

import com.youcode.covoiturageapi.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConducteurRepository extends JpaRepository<Conducteur,Long> {

    // check if email exist
    boolean existsByEmail(String email);
    // check id numero de permis exist
    boolean existsByNumeroPermis(String numeroPermis);

    // check for duplicate email
    boolean existsByEmailAndIdNot(String email, Long id);

    // check for duplicate Numero de Permis
    boolean existsByNumeroPermisAndIdNot(String numeroPermis, Long id);

    // search by name or email
    List<Conducteur> findByNomContainingIgnoreCaseOrEmailContainingIgnoreCase(String nom, String email);
}
