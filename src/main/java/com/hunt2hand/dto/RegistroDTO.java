package com.hunt2hand.dto;

import com.hunt2hand.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroDTO {

    private String nombre;
    private String apellido;
    private String ubicacion;
    private String imagen;

    private Boolean activado = false;
    private Boolean baneado = false;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    private String email;
    private String username;
    private String password;
    private Rol rol = Rol.USER;

    public boolean isActivado() {
        return Boolean.TRUE.equals(activado);
    }

    public boolean isBaneado() {
        return Boolean.TRUE.equals(baneado);
    }
}
