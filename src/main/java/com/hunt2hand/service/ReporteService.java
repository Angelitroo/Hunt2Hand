package com.hunt2hand.service;

import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.enums.Motivo;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Reporte;
import com.hunt2hand.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<ReporteDTO> getAll() {
        List<Reporte> reportes = reporteRepository.findAll();

        if (reportes == null) {
            return Collections.emptyList();
        }

        return reportes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReporteDTO> getByNombreReportado(String nombre) {
        String patron = nombre + "%";

        List<Reporte> reportes = reporteRepository.findByReportado_NombreLikeIgnoreCase(patron);

        if (reportes == null) {
            return Collections.emptyList();
        }

        return reportes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        Reporte reporte = new Reporte();
        Perfil reportador = new Perfil();
        reportador.setId(reporteDTO.getReportado());
        Perfil reportado = new Perfil();
        reportado.setId(reporteDTO.getReportado());
        reporte.setReportador(reportador);
        reporte.setReportado(reportado);
        reporte.setMotivo(Motivo.valueOf(reporteDTO.getMotivo()));
        reporte.setFecha(reporteDTO.getFecha());
        reporteRepository.save(reporte);
        return convertToDto(reporte);
    }

    public void eliminarMiReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public ReporteDTO convertToDto(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO();
        dto.setId(reporte.getId());
        dto.setReportado(reporte.getReportado().getId());
        dto.setReportador(reporte.getReportador().getId());
        dto.setMotivo(reporte.getMotivo().toString());
        dto.setFecha(reporte.getFecha());
        return dto;
    }
}