package com.example.RentApartments.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    @Field("user_id")
    private Long user_id;

    @Field("akcja")
    private String akcja;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("opis")
    private String opis;

    public Log() {}

    public Log(Long user_id, String akcja, String opis) {
        this.user_id = user_id;
        this.akcja = akcja;
        this.opis = opis;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }

    public String getAkcja() { return akcja; }
    public void setAkcja(String akcja) { this.akcja = akcja; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
}
