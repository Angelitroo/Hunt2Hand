package com.hunt2hand.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroDTO {

    //TABLA PERFIL
    private  String nombre;
    private String apellido;
    private String ubicacion;
    private boolean baneado = false;

    // TABLA USUARIO
    private String username;
    private String password;
    private String rol;

}