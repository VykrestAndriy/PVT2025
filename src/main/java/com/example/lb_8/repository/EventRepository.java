package com.example.lb_8.repository;

import com.example.lb_8.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE e.name LIKE %:name%")
    List<Event> findByNameContaining(@Param("name") String name);
}