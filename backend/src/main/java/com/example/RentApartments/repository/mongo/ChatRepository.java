package com.example.RentApartments.repository.mongo;

import com.example.RentApartments.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    Optional<Chat> findByMieszkanieIdAndUserId(Long mieszkanieId, Long userId);
    
    List<Chat> findByMieszkanieId(Long mieszkanieId);
    
    List<Chat> findByUserId(Long userId);
    
    @Query("{ 'mieszkanie_id': ?0, 'user_id': ?1 }")
    Optional<Chat> findChatForMieszkanie(Long mieszkanieId, Long userId);
}
