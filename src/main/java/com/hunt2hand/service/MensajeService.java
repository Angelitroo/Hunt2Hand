package com.hunt2hand.service;

import com.hunt2hand.dto.MensajeDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Mensaje;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.ChatRepository;
import com.hunt2hand.repository.MensajeRepository;
import com.hunt2hand.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ChatRepository chatRepository;
    private final PerfilRepository perfilRepository;

    public MensajeDTO enviarMensaje(MensajeDTO mensajeDTO) {
        Perfil emisor = perfilRepository.findById(mensajeDTO.getIdEmisor())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil emisor no encontrado"));

        Perfil receptor = perfilRepository.findById(mensajeDTO.getIdReceptor())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil receptor no encontrado"));

        Chat chat = chatRepository.findById(mensajeDTO.getIdChat())
                .orElseThrow(() -> new RecursoNoEncontrado("Chat no encontrado"));

        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setContenido(mensajeDTO.getContenido());
        try {
            mensaje.setFecha(LocalDateTime.parse(mensajeDTO.getFecha(), DateTimeFormatter.ISO_DATE_TIME));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido");
        }
        mensajeRepository.save(mensaje);
        return convertirAMensajeDTO(mensaje);
    }

    public List<MensajeDTO> obtenerMensajesPorChat(Long idChat) {
        List<Mensaje> mensajes = mensajeRepository.findMessagesByChatId(idChat);
        if (mensajes.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron mensajes para el chat con ID " + idChat);
        }
        return mensajes.stream()
                .map(this::convertirAMensajeDTO)
                .collect(Collectors.toList());
    }

    public List<MensajeDTO> obtenerMensajesEnviados(Long idUsuario) {
        List<Mensaje> mensajes = mensajeRepository.findSentMessages(idUsuario);
        if (mensajes.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron mensajes enviados por el usuario con ID " + idUsuario);
        }
        return mensajes.stream()
                .map(this::convertirAMensajeDTO)
                .collect(Collectors.toList());
    }

    public List<MensajeDTO> obtenerMensajesRecibidos(Long idUsuario) {
        List<Mensaje> mensajes = mensajeRepository.findReceivedMessages(idUsuario);
        if (mensajes.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron mensajes recibidos por el usuario con ID " + idUsuario);
        }
        return mensajes.stream()
                .map(this::convertirAMensajeDTO)
                .collect(Collectors.toList());
    }

    private MensajeDTO convertirAMensajeDTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setIdChat(mensaje.getChat().getId());
        dto.setIdEmisor(mensaje.getEmisor().getId());
        dto.setIdReceptor(mensaje.getReceptor().getId());
        dto.setContenido(mensaje.getContenido());
        dto.setFecha(mensaje.getFecha().toString());
        return dto;
    }

}
