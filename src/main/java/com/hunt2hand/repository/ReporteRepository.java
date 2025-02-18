package com.hunt2hand.repository;

import com.hunt2hand.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByReportado_IdOrReportador_Id(Long idReportado, Long idReportador);

    @Query("SELECT r FROM Reporte r WHERE LOWER(r.reportado.nombre) LIKE LOWER(:nombre)")
    List<Reporte> findByReportado_NombreLikeIgnoreCase(@Param("nombre") String nombre);

    @Query("SELECT r FROM Reporte r WHERE r.reportador.id = :idReportador AND r.reportado.id = :idReportado")
    Optional<Reporte> findPerfiles(@Param("idReportador") Long idReportador, @Param("idReportado") Long idReportado);
}

