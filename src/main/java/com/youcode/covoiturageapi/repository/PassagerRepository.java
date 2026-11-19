package com.youcode.covoiturageapi.repository;

import com.youcode.covoiturageapi.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassagerRepository extends JpaRepository<Passager,Long> {
    // check if email exist
    boolean existsByEmail(String email);
    // check if email is duplicate
    boolean existsByEmailAndIdNot(String email, Long id);
    //search by email
    Optional<Passager> findByEmail(String email);
}
