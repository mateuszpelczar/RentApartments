package com.example.RentApartments.controller;

import com.example.RentApartments.dto.RezerwacjaDTO;
import com.example.RentApartments.service.RezerwacjaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rezerwacje")
@Tag(name = "Rezerwacje", description = "API do zarządzania rezerwacjami apartamentów")
public class RezerwacjaController {

    private final RezerwacjaService rezerwacjaService;

    public RezerwacjaController(RezerwacjaService rezerwacjaService) {
        this.rezerwacjaService = rezerwacjaService;
    }

    @PostMapping
    @Operation(summary = "Utwórz nową rezerwację", description = "Tworzy nową rezerwację apartamentu dla zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rezerwacja utworzona pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe lub konflikt dat"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<RezerwacjaDTO> createRezerwacja(@RequestBody RezerwacjaDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RezerwacjaDTO created = rezerwacjaService.createRezerwacja(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz szczegóły rezerwacji", description = "Pobiera szczegóły konkretnej rezerwacji")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szczegóły rezerwacji"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie znaleziona")
    })
    public ResponseEntity<RezerwacjaDTO> getRezerwacja(
            @Parameter(description = "ID rezerwacji")
            @PathVariable Long id) {
        RezerwacjaDTO dto = rezerwacjaService.getRezerwacjaById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Pobierz moje rezerwacje", description = "Pobiera listę wszystkich rezerwacji zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista moich rezerwacji"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<List<RezerwacjaDTO>> getMyRezerwacje() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<RezerwacjaDTO> rezerwacje = rezerwacjaService.getUserRezerwacje(userId);
        return ResponseEntity.ok(rezerwacje);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Potwierdź rezerwację (ADMIN)", description = "Właściciel/Administrator potwierdza rezerwację apartamentu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rezerwacja potwierdzona pomyślnie"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie znaleziona")
    })
    public ResponseEntity<String> confirmRezerwacja(
            @Parameter(description = "ID rezerwacji do potwierdzenia")
            @PathVariable Long id) {
        rezerwacjaService.confirmRezerwacja(id);
        return ResponseEntity.ok("Rezerwacja potwiedzona");
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Anuluj rezerwację", description = "Anuluje rezerwację apartamentu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rezerwacja anulowana pomyślnie"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień"),
            @ApiResponse(responseCode = "404", description = "Rezerwacja nie znaleziona")
    })
    public ResponseEntity<String> cancelRezerwacja(
            @Parameter(description = "ID rezerwacji do anulowania")
            @PathVariable Long id) {
        rezerwacjaService.cancelRezerwacja(id);
        return ResponseEntity.ok("Rezerwacja anulowana");
    }
}