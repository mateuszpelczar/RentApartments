package com.example.RentApartments.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "chats")
public class Chat {

  @Id
  private String id;

  @Field("mieszkanie_id")
  private Long mieszkanieId;

  @Field("user_id")
  private Long userId;

  @Field("wiadomosci")
  private List<Wiadomosc> wiadomosci;

  @Field("created_at")
  private LocalDateTime createdAt;

  @Field("updated_at")
  private LocalDateTime updatedAt;

  public Chat(){}

  public Chat(Long mieszkanieId, Long userId) {
    this.mieszkanieId = mieszkanieId;
    this.userId = userId;
    this.wiadomosci = new ArrayList<>();
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public Long getMieszkanieId() { return mieszkanieId; }
  public void setMieszkanieId(Long mieszkanieId) { this.mieszkanieId = mieszkanieId; }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public List<Wiadomosc> getWiadomosci() { return wiadomosci; }
  public void setWiadomosci(List<Wiadomosc> wiadomosci) { this.wiadomosci = wiadomosci; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

  public static class Wiadomosc {
    @Field("user_id")
    private Long userId;
    
    @Field("tresc")
    private String tresc;
    
    @Field("timestamp")
    private LocalDateTime timestamp;

    public Wiadomosc() {}
    
    public Wiadomosc(Long userId, String tresc, LocalDateTime timestamp) {
      this.userId = userId;
      this.tresc = tresc;
      this.timestamp = timestamp;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTresc() { return tresc; }
    public void setTresc(String tresc) { this.tresc = tresc; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
  }
}
