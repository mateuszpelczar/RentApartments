package com.example.RentApartments.service;

import com.example.RentApartments.dto.LoginRequest;
import com.example.RentApartments.dto.RegisterRequest;
import com.example.RentApartments.dto.AuthResponse;
import com.example.RentApartments.model.User;
import com.example.RentApartments.model.Rola;
import com.example.RentApartments.model.UserRole;
import com.example.RentApartments.repository.jpa.UserRepository;
import com.example.RentApartments.repository.jpa.RolaRepository;
import com.example.RentApartments.repository.jpa.UserRoleRepository;
import com.example.RentApartments.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final RolaRepository rolaRepository;
  private final UserRoleRepository userRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final LogService logService;
  
  public AuthenticationService(UserRepository userRepository,
                                 RolaRepository rolaRepository,
                                 UserRoleRepository userRoleRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtTokenProvider jwtTokenProvider,
                                 LogService logService) {
        this.userRepository = userRepository;
        this.rolaRepository = rolaRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.logService = logService;
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Uzytkownik nie zostal znaleziony");
        }

        User user = userOpt.get();
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logService.logEvent(user.getId(), "LOGOWANIE_NIEUDANE", 
                "Nieudana próba zalogowania - błędne hasło dla użytkownika: " + request.getEmail());
            throw new RuntimeException("Nieprawidlowe dane");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        logService.logEvent(user.getId(), "LOGOWANIE_UDANE", 
            "Pomyślne zalogowanie użytkownika: " + user.getEmail());

        return new AuthResponse(accessToken, refreshToken, user.getId(), user.getEmail(), user.getUsername());
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Konto z tym adresem email juz istnieje");
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        User savedUser = userRepository.save(user);

        Rola userRole = rolaRepository.findByRolename("USER")
                .orElseThrow(() -> new RuntimeException("Rola USER nie zostala znaleziona"));

        UserRole userRoleAssignment = new UserRole();
        userRoleAssignment.setUser(savedUser);
        userRoleAssignment.setRole(userRole);
        userRoleRepository.save(userRoleAssignment);

        logService.logEvent(savedUser.getId(), "REJESTRACJA", 
            "Nowy użytkownik zarejestrowany: " + savedUser.getEmail() + ", imię: " + savedUser.getUsername() + ", telefon: " + savedUser.getPhoneNumber());

        String accessToken = jwtTokenProvider.generateAccessToken(savedUser.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getId());

        return new AuthResponse(accessToken, refreshToken, savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());
    }

    public boolean validateToken(String token) {
        try {
            jwtTokenProvider.getUserIdFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

}
