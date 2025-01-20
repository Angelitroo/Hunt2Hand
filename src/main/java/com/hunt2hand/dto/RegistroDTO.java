package com.hunt2hand.dto;

import com.hunt2hand.enums.Rol;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    private String username;
    private String password;
    private Rol rol;

}