package com.hunt2hand.repository;

import com.hunt2hand.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByPerfilValorador_IdOrPerfilValorado_Id(Long idValorador, Long idValorado);
}
