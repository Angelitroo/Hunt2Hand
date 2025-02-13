package com.hunt2hand.service;

import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Reporte;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ReporteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PerfilRepository perfilRepository;

    public List<ReporteDTO> getAll() {
        List<Reporte> reportes = reporteRepository.findAll();
        if (reportes.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron reportes");
        }
        return reportes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReporteDTO> getByNombreReportado(String nombre) {
        String patron = nombre + "%";
        List<Reporte> reportes = reporteRepository.findByReportado_NombreLikeIgnoreCase(patron);
        if (reportes == null || reportes.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron reportes para el nombre: " + nombre);
        }
        return reportes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Reporte crearReporte(Long idReportador, Long idReportado, ReporteDTO reporteDTO) {
        Perfil perfilReportador = perfilRepository.findById(idReportador)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil reportador no encontrado"));

        Perfil perfilReportado = perfilRepository.findById(idReportado)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil reportado no encontrado"));

        Reporte reporte = new Reporte();
        reporte.setReportador(perfilReportador);
        reporte.setReportado(perfilReportado);
        reporte.setMotivo(reporteDTO.getMotivo());
        reporte.setFecha(reporteDTO.getFecha());

        return reporteRepository.save(reporte);
    }

    public void eliminarMiReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Reporte con id " + id + " no encontrado");
        }
        reporteRepository.deleteById(id);
    }

    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Reporte con id " + id + " no encontrado");
        }
        reporteRepository.deleteById(id);
    }

    private ReporteDTO convertToDto(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO();
        dto.setId(reporte.getId());
        dto.setId_reportado(reporte.getReportado().getId());
        dto.setId_reportador(reporte.getReportador().getId());
        dto.setMotivo(reporte.getMotivo());
        dto.setFecha(reporte.getFecha());
        return dto;
    }

    public ReporteDTO buscarReporte(Long idReportador, Long idReportado) {
        perfilRepository.findById(idReportador)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil reportador no encontrado"));
        perfilRepository.findById(idReportado)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil reportado no encontrado"));

        Reporte reporte = reporteRepository.findPerfiles(idReportador, idReportado)
                .orElseThrow(() -> new RecursoNoEncontrado("Reporte no encontrado"));



        return convertToDto(reporte);
    }

    public void eliminarReportesByPerfil(Long id) {
        List<Reporte> reportes = reporteRepository.findByReportado_IdOrReportador_Id(id, id);
        reporteRepository.deleteAll(reportes);
    }
}