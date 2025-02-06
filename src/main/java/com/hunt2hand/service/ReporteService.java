package com.hunt2hand.service;

import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.enums.Motivo;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Reporte;
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

    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        Reporte reporte = new Reporte();
        Perfil reportador = new Perfil();
        reportador.setId(reporteDTO.getReportador());
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
        dto.setReportado(reporte.getReportado().getId());
        dto.setReportador(reporte.getReportador().getId());
        dto.setMotivo(reporte.getMotivo().toString());
        dto.setFecha(reporte.getFecha());
        return dto;
    }
}