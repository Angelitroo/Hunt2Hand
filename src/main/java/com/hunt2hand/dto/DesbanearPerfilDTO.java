package com.hunt2hand.dto;

import lombok.Data;

@Data
public class DesbanearPerfilDTO {
    private Long idPerfil;

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }
}
