package com.hunt2hand.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeguirDTO {
    private Long id;
    private Long idSeguidor;
    private Long idSeguido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSeguidor() {
        return idSeguidor;
    }

    public void setIdSeguidor(Long idSeguidor) {
        this.idSeguidor = idSeguidor;
    }

    public Long getIdSeguido() {
        return idSeguido;
    }

    public void setIdSeguido(Long idSeguido) {
        this.idSeguido = idSeguido;
    }
}