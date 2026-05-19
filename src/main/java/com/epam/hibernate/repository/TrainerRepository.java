package com.epam.hibernate.repository;

import com.epam.hibernate.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUserUsername(String username);
}