package com.example.RentApartments.service;

import com.example.RentApartments.model.Log;
import com.example.RentApartments.repository.mongo.LogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logEvent(Long userId, String action, String description) {
        Log log = new Log();
        log.setUser_id(userId);
        log.setAkcja(action);
        log.setTimestamp(LocalDateTime.now());

        logRepository.save(log);
    }

    public List<Log> getUserLogs(Long userId) {
        return logRepository.findByUser_id(userId);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    public List<Log> getLogsByAction(String action) {
        return logRepository.findByAkcja(action);
    }
}