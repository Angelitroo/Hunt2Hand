package com.hunt2hand.dto;

import lombok.Data;

@Data
public class ChatDTO {
    private Long id;
    private Long id_creador;
    private Long id_receptor;
    private String nombre_receptor;
    private String imagen_receptor;
}
