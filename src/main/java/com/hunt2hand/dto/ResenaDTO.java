package com.hunt2hand.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
public class ResenaDTO {
    private Long id;

    @Min(1)
    @Max(5)
    private Integer valoracion;
    private Long id_perfilvalorado;
    private Long id_perfilvalorador;
}
