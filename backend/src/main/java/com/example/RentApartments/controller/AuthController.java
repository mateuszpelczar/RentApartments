package com.example.RentApartments.controller;

import com.example.RentApartments.dto.LoginRequest;
import com.example.RentApartments.dto.RegisterRequest;
import com.example.RentApartments.dto.AuthResponse;
import com.example.RentApartments.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autentykacja", description = "API do rejestracji i logowania użytkowników")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Zarejestruj nowego użytkownika", description = "Tworzy nowe konto użytkownika w systemie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Użytkownik zarejestrowany pomyślnie, zwrócony JWT token"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe lub użytkownik już istnieje")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Zaloguj użytkownika", description = "Uwierzytelnia użytkownika i zwraca JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zalogowanie pomyślne, zwrócony JWT token"),
            @ApiResponse(responseCode = "401", description = "Błędne dane logowania")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    @Operation(summary = "Sprawdź poprawność tokenu", description = "Waliduje JWT token i zwraca informację czy jest ważny")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "True jeśli token jest ważny, false jeśli wygasł lub jest błędny")
    })
    public ResponseEntity<Boolean> validateToken(
            @Parameter(description = "Nagłówek autoryzacji z tokenem (Bearer <token>)")
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}