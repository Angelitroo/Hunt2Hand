package com.hunt2hand.controller;

import com.hunt2hand.dto.ChatDTO;
import com.hunt2hand.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/crear")
    public ResponseEntity<ChatDTO> crearChat(@RequestBody ChatDTO chatDTO) {
        ChatDTO chatCreado = chatService.crearChat(chatDTO);
        return chatCreado != null ? ResponseEntity.ok(chatCreado) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<ChatDTO>> obtenerChatsPorUsuario(@PathVariable Long idUsuario) {
        List<ChatDTO> chats = chatService.obtenerChatsPorUsuario(idUsuario);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/detalles/{idChat}")
    public ResponseEntity<ChatDTO> obtenerChatPorId(@PathVariable Long idChat) {
        ChatDTO chat = chatService.obtenerChatPorId(idChat);
        return chat != null ? ResponseEntity.ok(chat) : ResponseEntity.notFound().build();
    }
}
