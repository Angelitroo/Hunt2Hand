package com.hunt2hand.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ReseñaDTO {
    private Long id;
    private Integer valoracion;
    private Long id_perfilvalorado;
    private Long id_perfilvalorador;


}
