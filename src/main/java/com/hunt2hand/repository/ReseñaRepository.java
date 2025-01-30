package com.hunt2hand.repository;

import com.hunt2hand.model.Reseña;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseñaRepository extends JpaRepository<Reseña, Long> {
}
