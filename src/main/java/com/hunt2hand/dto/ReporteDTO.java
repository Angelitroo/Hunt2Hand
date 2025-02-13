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
}
