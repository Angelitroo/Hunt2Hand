package com.hunt2hand.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MensajeDTO {
    private Long id;
    private Long idChat;
    private Long idEmisor;
    private Long idReceptor;
    private String contenido;
    private String fecha;
}
