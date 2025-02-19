package com.hunt2hand.dto;

import lombok.Data;

@Data
public class CrearChatDTO {
    private Long id;
    private Long id_creador;
    private Long id_receptor;

    public CrearChatDTO(Long id, Long id_creador, Long id_receptor) {
        this.id = id;
        this.id_creador = id_creador;
        this.id_receptor = id_receptor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_creador() {
        return id_creador;
    }

    public void setId_creador(Long id_creador) {
        this.id_creador = id_creador;
    }

    public Long getId_receptor() {
        return id_receptor;
    }

    public void setId_receptor(Long id_receptor) {
        this.id_receptor = id_receptor;
    }
}
