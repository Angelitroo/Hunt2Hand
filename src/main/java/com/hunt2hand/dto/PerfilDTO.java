package com.hunt2hand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    private  String nombre;
    private String apellido;
    private String ubicacion;
    private String imagen;
    private Boolean baneado;
}