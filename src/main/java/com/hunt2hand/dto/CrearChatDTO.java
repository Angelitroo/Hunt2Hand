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
}
