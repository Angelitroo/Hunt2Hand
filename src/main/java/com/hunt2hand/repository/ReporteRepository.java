package com.hunt2hand.repository;

import com.hunt2hand.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByReportado_IdOrReportador_Id(Long idReportado, Long idReportador);

    @Query("SELECT r FROM Reporte r WHERE r.reportado.nombre LIKE :nombre")
    List<Reporte> findByReportado_NombreLikeIgnoreCase(String nombre);
}
