package com.hunt2hand.service;

import com.hunt2hand.dto.MensajeDTO;
import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Mensaje;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.ChatRepository;
import com.hunt2hand.repository.MensajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensajeService {
    private final MensajeRepository mensajeRepository;
    private final ChatRepository chatRepository;

    public MensajeDTO enviarMensaje(MensajeDTO mensajeDTO) {
        Perfil emisor = new Perfil();
        emisor.setId(mensajeDTO.getIdEmisor());

        Perfil receptor = new Perfil();
        receptor.setId(mensajeDTO.getIdReceptor());

        Chat chat = chatRepository.findChatBetweenUsers(emisor, receptor)
                .orElseGet(() -> {
                    Chat nuevoChat = new Chat();
                    nuevoChat.setCreador(emisor);
                    nuevoChat.setReceptor(receptor);
                    return chatRepository.save(nuevoChat);
                });

        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setContenido(mensajeDTO.getContenido());
        mensaje.setFecha(mensajeDTO.getFechaEnvio().toLocalDate());
        mensaje.setHora(mensajeDTO.getFechaEnvio().toLocalTime());

        mensaje = mensajeRepository.save(mensaje);

        return convertirAMensajeDTO(mensaje);
    }

    public List<MensajeDTO> obtenerMensajesPorChat(Long idChat) {
        return mensajeRepository.findMessagesByChatId(idChat).stream()
                .map(this::convertirAMensajeDTO)
                .collect(Collectors.toList());
    }

    public List<MensajeDTO> obtenerMensajesEnviados(Long idUsuario) {
        return mensajeRepository.findSentMessages(idUsuario).stream()
                .map(this::convertirAMensajeDTO)
                .collect(Collectors.toList());
    }

    public List<MensajeDTO> obtenerMensajesRecibidos(Long idUsuario) {
        return mensajeRepository.findReceivedMessages(idUsuario).stream()
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
        dto.setFechaEnvio(LocalDateTime.of(mensaje.getFecha(), mensaje.getHora()));
        return dto;
    }
}
