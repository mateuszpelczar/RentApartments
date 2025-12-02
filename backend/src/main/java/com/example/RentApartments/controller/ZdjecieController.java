package com.example.RentApartments.controller;

import com.example.RentApartments.model.Zdjecie;
import com.example.RentApartments.service.ZdjecieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zdjecia")
@Tag(name = "Zdjęcia", description = "API do zarządzania zdjęciami ogłoszeń")
public class ZdjecieController {

    private final ZdjecieService zdjecieService;

    public ZdjecieController(ZdjecieService zdjecieService) {
        this.zdjecieService = zdjecieService;
    }

    @PostMapping
    @Operation(summary = "Dodaj zdjęcie do ogłoszenia", description = "Dodaje nowe zdjęcie do ogłoszenia mieszkania")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zdjęcie dodane pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<Zdjecie> addZdjecie(@RequestBody Map<String, Object> request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long mieszkanieId = ((Number) request.get("mieszkanieId")).longValue();
        String url = (String) request.get("url");
        String opis = (String) request.get("opis");

        Zdjecie zdjecie = zdjecieService.uploadPhoto(mieszkanieId, url, opis, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(zdjecie);
    }

    @GetMapping("/mieszkanie/{mieszkanieId}")
    @Operation(summary = "Pobierz zdjęcia ogłoszenia", description = "Pobiera wszystkie zdjęcia danego ogłoszenia")
    @ApiResponse(responseCode = "200", description = "Lista zdjęć")
    public ResponseEntity<List<Zdjecie>> getZdjeciaByMieszkanieId(
            @Parameter(description = "ID ogłoszenia")
            @PathVariable Long mieszkanieId) {
        List<Zdjecie> zdjecia = zdjecieService.getMieszkaniePhotos(mieszkanieId);
        return ResponseEntity.ok(zdjecia);
    }

    @GetMapping("/{zdjecieId}")
    @Operation(summary = "Pobierz szczegóły zdjęcia", description = "Pobiera szczegóły konkretnego zdjęcia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szczegóły zdjęcia"),
            @ApiResponse(responseCode = "404", description = "Zdjęcie nie znalezione")
    })
    public ResponseEntity<Zdjecie> getZdjecieById(
            @Parameter(description = "ID zdjęcia")
            @PathVariable String zdjecieId) {
        Zdjecie zdjecie = zdjecieService.getPhotoById(zdjecieId);
        return ResponseEntity.ok(zdjecie);
    }

    @DeleteMapping("/{zdjecieId}")
    @Operation(summary = "Usuń zdjęcie", description = "Usuwa zdjęcie z ogłoszenia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zdjęcie usunięte"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany"),
            @ApiResponse(responseCode = "404", description = "Zdjęcie nie znalezione")
    })
    public ResponseEntity<Map<String, String>> deleteZdjecie(
            @Parameter(description = "ID zdjęcia")
            @PathVariable String zdjecieId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        zdjecieService.deletePhoto(zdjecieId, userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Zdjęcie usunięte pomyślnie");
        return ResponseEntity.ok(response);
    }
}
