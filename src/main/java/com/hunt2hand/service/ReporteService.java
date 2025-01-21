package com.hunt2hand.service;

import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.enums.Motivo;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Reporte;
import com.hunt2hand.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public Set<ReporteDTO> getAllReportes() {
        Set<ReporteDTO> reporteDTOS = new HashSet<>();
        for (Reporte reporte : reporteRepository.findAll()) {
            ReporteDTO reporteDTO = new ReporteDTO();
            reporteDTO.setId(reporte.getId());
            reporteDTO.setNombre_reportado(reporte.getReportado().getNombre());
            reporteDTO.setNombre_reportador(reporte.getReportador().getNombre());
            reporteDTO.setMotivo(reporte.getMotivo().toString());
            reporteDTOS.add(reporteDTO);
        }
        return reporteDTOS;
    }

    public Set<ReporteDTO> getReportesByNombreReportado(String nombreReportado) {
        Set<ReporteDTO> reporteDTOS = new HashSet<>();
        for (Reporte reporte : reporteRepository.findByReportadoNombre(nombreReportado)) {
            ReporteDTO reporteDTO = new ReporteDTO();
            reporteDTO.setId(reporte.getId());
            reporteDTO.setNombre_reportado(reporte.getReportado().getNombre());
            reporteDTO.setNombre_reportador(reporte.getReportador().getNombre());
            reporteDTO.setMotivo(reporte.getMotivo().toString());
            reporteDTOS.add(reporteDTO);
        }
        return reporteDTOS;
    }


    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        Reporte reporte = new Reporte();
        reporte.setReportador(new Perfil());
        reporte.getReportador().setNombre(reporteDTO.getNombre_reportador());
        reporte.setReportado(new Perfil());
        reporte.getReportado().setNombre(reporteDTO.getNombre_reportado());
        reporte.setMotivo(Motivo.valueOf(reporteDTO.getMotivo()));
        reporteRepository.save(reporte);
        return reporteDTO;
    }

    public void eliminarMiReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}
