package com.hunt2hand.service;

import com.hunt2hand.dto.ChatDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.ChatRepository;
import com.hunt2hand.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PerfilRepository perfilRepository;

    public ChatDTO crearChat(ChatDTO chatDTO) {
        Perfil usuario1 = perfilRepository.findById(chatDTO.getId_creador())
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario creador no encontrado"));
        Perfil usuario2 = perfilRepository.findById(chatDTO.getId_receptor())
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario receptor no encontrado"));

        Chat chat = new Chat();
        chat.setCreador(usuario1);
        chat.setReceptor(usuario2);
        chat = chatRepository.save(chat);

        return convertirAChatDTO(chat);
    }

    public List<ChatDTO> getChatById(Long idUsuario) {
        List<Chat> chats = chatRepository.findChatsByUserId(idUsuario);
        if (chats.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron chats para el usuario con ID " + idUsuario);
        }
        return chats.stream().map(this::convertirAChatDTO).collect(Collectors.toList());
    }

    public ChatDTO getDetallesChat(Long idChat) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new RecursoNoEncontrado("Chat no encontrado"));
        return convertirAChatDTO(chat);
    }

    private ChatDTO convertirAChatDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setId_creador(chat.getCreador().getId());
        dto.setId_receptor(chat.getReceptor().getId());
        dto.setNombre_receptor(chat.getReceptor().getNombre());
        dto.setImagen_receptor(chat.getReceptor().getImagen());
        return dto;
    }
}