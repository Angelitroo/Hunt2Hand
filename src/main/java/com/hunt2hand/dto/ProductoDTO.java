package com.hunt2hand.dto;

import com.hunt2hand.enums.Estado;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Estado estado;
    private String imagen;
    private Boolean vendido;
    private Long perfil;
}
