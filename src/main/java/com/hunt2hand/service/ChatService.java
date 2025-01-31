package com.hunt2hand.service;

import com.hunt2hand.dto.ChatDTO;
import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.ChatRepository;
import com.hunt2hand.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PerfilRepository perfilRepository;

    public ChatDTO crearChat(ChatDTO chatDTO) {
        Perfil usuario1 = perfilRepository.findById(chatDTO.getId_creador()).orElse(null);
        Perfil usuario2 = perfilRepository.findById(chatDTO.getId_receptor()).orElse(null);

        if (usuario1 == null || usuario2 == null) {
            return null;
        }

        Chat chat = new Chat();
        chat.setCreador(usuario1);
        chat.setReceptor(usuario2);
        chat = chatRepository.save(chat);

        return convertirAChatDTO(chat);
    }

    public List<ChatDTO> obtenerChatsPorUsuario(Long idUsuario) {
        List<Chat> chats = chatRepository.findChatsByUserId(idUsuario);
        return chats.stream().map(this::convertirAChatDTO).collect(Collectors.toList());
    }

    public ChatDTO obtenerChatPorId(Long idChat) {
        Optional<Chat> chat = chatRepository.findById(idChat);
        return chat.map(this::convertirAChatDTO).orElse(null);
    }

    private ChatDTO convertirAChatDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setId_creador(chat.getCreador().getId());
        dto.setId_receptor(chat.getReceptor().getId());
        return dto;
    }
}
