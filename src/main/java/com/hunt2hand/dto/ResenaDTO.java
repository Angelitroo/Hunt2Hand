package com.hunt2hand.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
public class ResenaDTO {
    private Long id;

    @Min(1)
    @Max(5)
    private Integer valoracion;
    private Long id_perfilvalorado;
    private Long id_perfilvalorador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public Long getId_perfilvalorado() {
        return id_perfilvalorado;
    }

    public void setId_perfilvalorado(Long id_perfilvalorado) {
        this.id_perfilvalorado = id_perfilvalorado;
    }

    public Long getId_perfilvalorador() {
        return id_perfilvalorador;
    }

    public void setId_perfilvalorador(Long id_perfilvalorador) {
        this.id_perfilvalorador = id_perfilvalorador;
    }
}
