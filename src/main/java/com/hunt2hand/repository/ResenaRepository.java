package com.hunt2hand.repository;

import com.hunt2hand.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByPerfilValorador_IdOrPerfilValorado_Id(Long idValorador, Long idValorado);

    @Query("SELECT AVG(r.valoracion) FROM Resena r WHERE r.perfilValorado.id = :idPerfilValorado")
    Double findMediaValoracion(Long idPerfilValorado);

    @Query("SELECT r FROM Resena r WHERE r.perfilValorador.id = :idPerfilValorador AND r.perfilValorado.id = :idPerfilValorado")
    Optional<Resena> findPerfiles(@Param("idPerfilValorador") Long idPerfilValorador, @Param("idPerfilValorado") Long idPerfilValorado);
}