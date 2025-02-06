package com.hunt2hand.controller;

import com.hunt2hand.dto.MensajeDTO;
import com.hunt2hand.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensaje")
@RequiredArgsConstructor
public class MensajeController {
    private final MensajeService mensajeService;

    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO> enviarMensaje(@RequestBody MensajeDTO mensajeDTO) {
        MensajeDTO mensajeCreado = mensajeService.enviarMensaje(mensajeDTO);
        return mensajeCreado != null ? ResponseEntity.ok(mensajeCreado) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/chat/{idChat}")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesPorChat(@PathVariable Long idChat) {
        List<MensajeDTO> mensajes = mensajeService.obtenerMensajesPorChat(idChat);
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/enviados/{idUsuario}")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesEnviados(@PathVariable Long idUsuario) {
        List<MensajeDTO> mensajes = mensajeService.obtenerMensajesEnviados(idUsuario);
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/recibidos/{idUsuario}")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesRecibidos(@PathVariable Long idUsuario) {
        List<MensajeDTO> mensajes = mensajeService.obtenerMensajesRecibidos(idUsuario);
        return ResponseEntity.ok(mensajes);
    }
}
