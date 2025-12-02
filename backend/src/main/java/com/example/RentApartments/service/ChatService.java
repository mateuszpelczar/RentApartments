package com.example.RentApartments.service;

import com.example.RentApartments.model.Chat;
import com.example.RentApartments.repository.mongo.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final LogService logService;

    public ChatService(ChatRepository chatRepository, LogService logService) {
        this.chatRepository = chatRepository;
        this.logService = logService;
    }

    public Chat sendMessage(Long userId, Long mieszkanieId, String message) {
        
        Optional<Chat> existingChat = chatRepository.findByMieszkanieIdAndUserId(mieszkanieId, userId);
        
        Chat chat;
        if (existingChat.isPresent()) {
            chat = existingChat.get();
        } else {
          
            chat = new Chat(mieszkanieId, userId);
        }
        
        
        Chat.Wiadomosc wiadomosc = new Chat.Wiadomosc(userId, message, LocalDateTime.now());
        chat.getWiadomosci().add(wiadomosc);
        chat.setUpdatedAt(LocalDateTime.now());
        
        Chat savedChat = chatRepository.save(chat);
        
        
        logService.logEvent(userId, "WIADOMOSC_WYSŁANA", 
            "Wiadomość wysłana na temat mieszkania ID: " + mieszkanieId);
        
        return savedChat;
    }

    public Optional<Chat> getChatForMieszkanie(Long mieszkanieId, Long userId) {
        return chatRepository.findByMieszkanieIdAndUserId(mieszkanieId, userId);
    }

    public List<Chat> getMieszkanieChats(Long mieszkanieId) {
        return chatRepository.findByMieszkanieId(mieszkanieId);
    }

    public List<Chat> getUserChats(Long userId) {
        return chatRepository.findByUserId(userId);
    }

    public void deleteChat(String chatId) {
        chatRepository.deleteById(chatId);
    }
}
