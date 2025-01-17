package com.hunt2hand.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilCrearDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private  String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @Email(message = "El ubicacion introducido no es válido")
    private String ubicacion;

    @Email(message = "El imagen introducido no es válido")
    private String imagen;

    private String baneado;

}