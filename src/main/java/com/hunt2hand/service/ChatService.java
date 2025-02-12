package com.hunt2hand.service;

import com.hunt2hand.dto.ChatDTO;
import com.hunt2hand.dto.CrearChatDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.ChatRepository;
import com.hunt2hand.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PerfilRepository perfilRepository;

    public CrearChatDTO crearChat(CrearChatDTO crearChatDTO) {
        Long idCreador = crearChatDTO.getId_creador();
        Long idReceptor = crearChatDTO.getId_receptor();

        // Buscar si ya existe el chat
        Optional<Chat> chatExistente = chatRepository.findChatBetweenProfiles(idCreador, idReceptor);

        if (chatExistente.isPresent()) {
            Chat chat = chatExistente.get();
            return new CrearChatDTO(chat.getId(), chat.getCreador().getId(), chat.getReceptor().getId());
        }

        // Crear un nuevo chat si no existe
        Perfil creador = perfilRepository.findById(idCreador)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil creador no encontrado"));
        Perfil receptor = perfilRepository.findById(idReceptor)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil receptor no encontrado"));

        Chat nuevoChat = new Chat();
        nuevoChat.setCreador(creador);
        nuevoChat.setReceptor(receptor);
        Chat chatCreado = chatRepository.save(nuevoChat);

        return new CrearChatDTO(chatCreado.getId(), chatCreado.getCreador().getId(), chatCreado.getReceptor().getId());
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