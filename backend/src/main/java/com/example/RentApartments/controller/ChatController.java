package com.example.RentApartments.controller;

import com.example.RentApartments.model.Chat;
import com.example.RentApartments.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Wiadomości", description = "API do komunikacji między użytkownikami dotyczącej apartamentów")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    @Operation(summary = "Wyślij wiadomość", description = "Wysyła wiadomość na temat konkretnego apartamentu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wiadomość wysłana pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane lub pusta wiadomość"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
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
    @Operation(summary = "Pobierz wszystkie wiadomości na apartament", description = "Pobiera wszystkie wiadomości dla konkretnego apartamentu (dostęp: admin/właściciel)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista wiadomości na apartament"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany"),
            @ApiResponse(responseCode = "403", description = "Brak dostępu")
    })
    public ResponseEntity<List<Chat>> getMieszkanieChats(
            @Parameter(description = "ID apartamentu")
            @PathVariable Long mieszkanieId) {
        List<Chat> chats = chatService.getMieszkanieChats(mieszkanieId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/mieszkanie/{mieszkanieId}/my-conversation")
    @Operation(summary = "Pobierz moją konwersację na apartament", description = "Pobiera konwersację zalogowanego użytkownika dotyczącą konkretnego apartamentu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Konwersacja użytkownika"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<?> getMyConversationForMieszkanie(
            @Parameter(description = "ID apartamentu")
            @PathVariable Long mieszkanieId) {
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
    @Operation(summary = "Pobierz moje konwersacje", description = "Pobiera listę wszystkich konwersacji zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista moich konwersacji"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<List<Chat>> getMyConversations() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> chats = chatService.getUserChats(userId);
        return ResponseEntity.ok(chats);
    }

    @DeleteMapping("/{chatId}")
    @Operation(summary = "Usuń konwersację", description = "Usuwa całą konwersację (tylko właściciel)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Konwersacja usunięta pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień"),
            @ApiResponse(responseCode = "400", description = "Błąd przy usuwaniu")
    })
    public ResponseEntity<String> deleteChat(
            @Parameter(description = "ID konwersacji do usunięcia")
            @PathVariable String chatId) {
        try {
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Czat usunięty pomyślnie");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Blad przy usuwaniu czatu: " + e.getMessage());
        }
    }
}