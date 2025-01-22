package com.hunt2hand.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private BigDecimal precio;
    private String estado;
    private String imagen;
    private Boolean vendido;
    private Long perfil;
}
