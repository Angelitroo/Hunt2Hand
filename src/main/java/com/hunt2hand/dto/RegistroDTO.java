package com.hunt2hand.dto;

import com.hunt2hand.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroDTO {

    //TABLA PERFIL
    private String nombre;
    private String apellido;
    private String ubicacion;
    private Boolean activado = false;
    private boolean baneado = false;
    private String imagen;

    // TABLA USUARIO
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    private String email;
    private String username;
    private String password;
    private Rol rol = Rol.USER;

    public boolean isActivado() {
        return activado != null && activado;
    }
}