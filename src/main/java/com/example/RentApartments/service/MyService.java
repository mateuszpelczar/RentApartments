package com.example.RentApartments.service;
import com.example.RentApartments.model.Log;
import com.example.RentApartments.model.User;
import com.example.RentApartments.repository.jpa.UserRepository;
import com.example.RentApartments.repository.mongo.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public MyService(UserRepository userRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Log createLog(Log log) {
        return logRepository.save(log);
    }
}
