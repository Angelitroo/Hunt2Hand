package com.hunt2hand.controller;

import com.hunt2hand.dto.ChatDTO;
import com.hunt2hand.dto.CrearChatDTO;
import com.hunt2hand.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private final ChatService chatService;

    @PostMapping("/crear")
    public ResponseEntity<CrearChatDTO> crearChat(@RequestBody CrearChatDTO crearChatDTO) {
        CrearChatDTO chatCreado = chatService.crearChat(crearChatDTO);
        return ResponseEntity.ok(chatCreado);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<ChatDTO>> getChatById(@PathVariable Long idUsuario) {
        List<ChatDTO> chats = chatService.getChatById(idUsuario);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/detalles/{idChat}")
    public ResponseEntity<ChatDTO> getDetallesChat(@PathVariable Long idChat) {
        ChatDTO chat = chatService.getDetallesChat(idChat);
        return chat != null ? ResponseEntity.ok(chat) : ResponseEntity.notFound().build();
    }
}
