package com.hunt2hand.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteDTO {
    private Long id;
    private LocalDate fecha;
    private Long id_reportador;
    private Long id_reportado;
    private String motivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getId_reportador() {
        return id_reportador;
    }

    public void setId_reportador(Long id_reportador) {
        this.id_reportador = id_reportador;
    }

    public Long getId_reportado() {
        return id_reportado;
    }

    public void setId_reportado(Long id_reportado) {
        this.id_reportado = id_reportado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
