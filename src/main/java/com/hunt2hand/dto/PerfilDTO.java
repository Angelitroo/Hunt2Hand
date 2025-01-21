package com.hunt2hand.dto;

import lombok.Data;

@Data
public class PerfilDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String ubicacion;
    private String imagen;
    private Boolean baneado;
}