package com.example.RentApartments.controller;

import com.example.RentApartments.model.Chat;
import com.example.RentApartments.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long mieszkanieId = ((Number) request.get("mieszkanieId")).longValue();
            String message = (String) request.get("message");

            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Wiadomosc nie moze byc pusta");
            }

            Chat chat = chatService.sendMessage(userId, mieszkanieId, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(chat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Blad przy wysylaniu wiadomosci: " + e.getMessage());
        }
    }

    @GetMapping("/mieszkanie/{mieszkanieId}")
    public ResponseEntity<List<Chat>> getMieszkanieChats(@PathVariable Long mieszkanieId) {
        List<Chat> chats = chatService.getMieszkanieChats(mieszkanieId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/mieszkanie/{mieszkanieId}/my-conversation")
    public ResponseEntity<?> getMyConversationForMieszkanie(@PathVariable Long mieszkanieId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Chat> chat = chatService.getChatForMieszkanie(mieszkanieId, userId);
        
        if (chat.isPresent()) {
            return ResponseEntity.ok(chat.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Brak wiadomosci dla tego mieszkania");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/my-conversations")
    public ResponseEntity<List<Chat>> getMyConversations() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> chats = chatService.getUserChats(userId);
        return ResponseEntity.ok(chats);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable String chatId) {
        try {
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Czat usunięty pomyślnie");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Blad przy usuwaniu czatu: " + e.getMessage());
        }
    }
}