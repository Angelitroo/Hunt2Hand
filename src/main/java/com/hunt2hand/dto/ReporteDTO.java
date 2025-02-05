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
    private Long reportador;
    private Long reportado;
    private String motivo;
}
