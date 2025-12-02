package com.example.RentApartments.service;

import com.example.RentApartments.dto.UserDTO;
import com.example.RentApartments.model.User;
import com.example.RentApartments.repository.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));
        return mapToDTO(user);
    }

    public UserDTO updateProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getSurname() != null) {
            user.setSurname(userDTO.getSurname());
        }

        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Stare hasla jest niepoprawne");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getSurname(),
            user.getEmail(),
            user.getPhoneNumber()
        );
    }
}