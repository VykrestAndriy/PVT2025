package com.example.lb_9.repository;

import com.example.lb_9.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByName(String name);
}