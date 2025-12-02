package com.example.RentApartments.controller;

import com.example.RentApartments.dto.RezerwacjaDTO;
import com.example.RentApartments.service.RezerwacjaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rezerwacje")
public class RezerwacjaController {

    private final RezerwacjaService rezerwacjaService;

    public RezerwacjaController(RezerwacjaService rezerwacjaService) {
        this.rezerwacjaService = rezerwacjaService;
    }

    @PostMapping
    public ResponseEntity<RezerwacjaDTO> createRezerwacja(@RequestBody RezerwacjaDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RezerwacjaDTO created = rezerwacjaService.createRezerwacja(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RezerwacjaDTO> getRezerwacja(@PathVariable Long id) {
        RezerwacjaDTO dto = rezerwacjaService.getRezerwacjaById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<RezerwacjaDTO>> getMyRezerwacje() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<RezerwacjaDTO> rezerwacje = rezerwacjaService.getUserRezerwacje(userId);
        return ResponseEntity.ok(rezerwacje);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmRezerwacja(@PathVariable Long id) {
        rezerwacjaService.confirmRezerwacja(id);
        return ResponseEntity.ok("Rezerwacja potwiedzona");
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelRezerwacja(@PathVariable Long id) {
        rezerwacjaService.cancelRezerwacja(id);
        return ResponseEntity.ok("Rezerwacja anulowana");
    }
}