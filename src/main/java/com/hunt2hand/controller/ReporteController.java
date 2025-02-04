package com.hunt2hand.controller;

import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public Set<ReporteDTO> getAllReportes() {
        return reporteService.getAllReportes();
    }

    @GetMapping("/{nombre_reportado}")
    public ResponseEntity<Set<ReporteDTO>> getReportesByNombreReportado(@PathVariable String nombre_reportado) {
        Set<ReporteDTO> reportes = reporteService.getReportesByNombreReportado(nombre_reportado);
        return ResponseEntity.ok(reportes);
    }

    @PostMapping("/crear/{}")
    public ResponseEntity<ReporteDTO> crearReporte(@RequestBody ReporteDTO reporteDTO) {
        ReporteDTO reporteCreado = reporteService.crearReporte(reporteDTO);
        return ResponseEntity.ok(reporteCreado);
    }

    @DeleteMapping("eliminarmio/{id}")
    public ResponseEntity<Void> eliminarMiReporte(@PathVariable Long id) {
        reporteService.eliminarMiReporte(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}