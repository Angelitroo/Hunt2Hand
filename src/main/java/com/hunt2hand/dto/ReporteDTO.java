package com.hunt2hand.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteDTO {
    private Long id;
    private Long id_reportador;
    private Long id_reportado;
    private String nombre_reportado;
    private String nombre_reportador;
    private String motivo;
}
