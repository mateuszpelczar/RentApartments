package com.example.RentApartments.controller;

import com.example.RentApartments.dto.MieszkanieDTO;
import com.example.RentApartments.service.MieszkanieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mieszkania")
@Tag(name = "Mieszkania", description = "API do zarządzania ogłoszeniami apartamentów (mieszkań)")
public class MieszkanieController {

    private final MieszkanieService mieszkanieService;

    public MieszkanieController(MieszkanieService mieszkanieService) {
        this.mieszkanieService = mieszkanieService;
    }

    @PostMapping
    @Operation(summary = "Utworz nowe ogłoszenie", description = "Tworzy nowe ogłoszenie mieszkania przez zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ogłoszenie utworzone pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<MieszkanieDTO> createMieszkanie(@RequestBody MieszkanieDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MieszkanieDTO created = mieszkanieService.createMieszkanie(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz szczegóły ogłoszenia", description = "Pobiera szczegóły konkretnego ogłoszenia mieszkania")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szczegóły ogłoszenia"),
            @ApiResponse(responseCode = "404", description = "Ogłoszenie nie znalezione")
    })
    public ResponseEntity<MieszkanieDTO> getMieszkanie(
            @Parameter(description = "ID ogłoszenia")
            @PathVariable Long id) {
        MieszkanieDTO dto = mieszkanieService.getMieszkanieById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zatwierdzone ogłoszenia", description = "Pobiera listę wszystkich zatwierdzonych ogłoszeń mieszkań")
    @ApiResponse(responseCode = "200", description = "Lista ogłoszeń")
    public ResponseEntity<List<MieszkanieDTO>> getAllApproved() {
        List<MieszkanieDTO> mieszkania = mieszkanieService.getAllApproved();
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/my-listings")
    @Operation(summary = "Pobierz moje ogłoszenia", description = "Pobiera listę ogłoszeń mieszkań zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista moich ogłoszeń"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany")
    })
    public ResponseEntity<List<MieszkanieDTO>> getMyListings() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MieszkanieDTO> mieszkania = mieszkanieService.getUserMieszkania(userId);
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/filter/city")
    @Operation(summary = "Filtruj ogłoszenia po mieście", description = "Pobiera ogłoszenia mieszkań z podanego miasta")
    @ApiResponse(responseCode = "200", description = "Lista ogłoszeń z danego miasta")
    public ResponseEntity<List<MieszkanieDTO>> filterByCity(
            @Parameter(description = "Nazwa miasta")
            @RequestParam String city) {
        List<MieszkanieDTO> mieszkania = mieszkanieService.filterByCity(city);
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/filter/price")
    @Operation(summary = "Filtruj ogłoszenia po cenie", description = "Pobiera ogłoszenia mieszkań w podanym przedziale cenowym")
    @ApiResponse(responseCode = "200", description = "Lista ogłoszeń w przedziale ceny")
    public ResponseEntity<List<MieszkanieDTO>> filterByPrice(
            @Parameter(description = "Minimalna cena (PLN/miesiąc)")
            @RequestParam Double minPrice,
            @Parameter(description = "Maksymalna cena (PLN/miesiąc)")
            @RequestParam Double maxPrice) {
        List<MieszkanieDTO> mieszkania = mieszkanieService.filterByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(mieszkania);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edytuj ogłoszenie", description = "Edytuje istniejące ogłoszenie mieszkania (tylko właściciel)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ogłoszenie edytowane pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do edytowania"),
            @ApiResponse(responseCode = "404", description = "Ogłoszenie nie znalezione")
    })
    public ResponseEntity<MieszkanieDTO> updateMieszkanie(
            @Parameter(description = "ID ogłoszenia")
            @PathVariable Long id,
            @RequestBody MieszkanieDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MieszkanieDTO updated = mieszkanieService.updateMieszkanie(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń ogłoszenie", description = "Usuwa ogłoszenie mieszkania (tylko właściciel)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ogłoszenie usunięte pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik nie zalogowany"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do usunięcia"),
            @ApiResponse(responseCode = "404", description = "Ogłoszenie nie znalezione")
    })
    public ResponseEntity<String> deleteMieszkanie(
            @Parameter(description = "ID ogłoszenia")
            @PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mieszkanieService.deleteMieszkanie(id, userId);
        return ResponseEntity.ok("Mieszkanie usunieto pomyslnie");
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Zatwierdź ogłoszenie (ADMIN)", description = "Administrator zatwierdza ogłoszenie mieszkania")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ogłoszenie zatwierdzone"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień administratora"),
            @ApiResponse(responseCode = "404", description = "Ogłoszenie nie znalezione")
    })
    public ResponseEntity<String> approveMieszkanie(
            @Parameter(description = "ID ogłoszenia do zatwierdzenia")
            @PathVariable Long id) {
        mieszkanieService.approveMieszkanie(id);
        return ResponseEntity.ok("Mieszkanie zatwierdzone");
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Odrzuć ogłoszenie (ADMIN)", description = "Administrator odrzuca ogłoszenie mieszkania")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ogłoszenie odrzucone"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień administratora"),
            @ApiResponse(responseCode = "404", description = "Ogłoszenie nie znalezione")
    })
    public ResponseEntity<String> rejectMieszkanie(
            @Parameter(description = "ID ogłoszenia do odrzucenia")
            @PathVariable Long id) {
        mieszkanieService.rejectMieszkanie(id);
        return ResponseEntity.ok("Mieszkanie odrzucone");
    }
}