package com.example.RentApartments.controller;

import com.example.RentApartments.dto.UserDTO;
import com.example.RentApartments.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "Użytkownicy", description = "API do zarządzania profilami użytkowników")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz dane użytkownika", description = "Pobiera dane publiczne konkretnego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dane użytkownika"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID użytkownika")
            @PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/profile")
    @Operation(summary = "Pobierz mój profil", description = "Pobiera dane profilu zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dane mojego profilu"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<UserDTO> getProfile() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    @Operation(summary = "Aktualizuj mój profil", description = "Aktualizuje dane profilu zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil zaktualizowany pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO updated = userService.updateProfile(userId, userDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Zmień hasło", description = "Zmienia hasło zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hasło zmienione pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne stare hasło"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody Map<String, String> request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        
        userService.changePassword(userId, oldPassword, newPassword);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Haslo zostalo zmienione pomyslnie");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/account")
    @Operation(summary = "Usuń moje konto", description = "Permanentnie usuwa konto zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Konto usunięte pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<Map<String, String>> deleteAccount() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUser(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Konto zostalo usuniete pomyslnie");
        return ResponseEntity.ok(response);
    }
}