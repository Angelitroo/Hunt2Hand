package com.hunt2hand.dto;

import lombok.Data;

@Data
public class PerfilActualizarDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String ubicacion;
    private String imagen;
    private String email;
    private String username;
    private String password;
}
