package com.example.RentApartments.controller;

import com.example.RentApartments.dto.MieszkanieDTO;
import com.example.RentApartments.service.MieszkanieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mieszkania")
public class MieszkanieController {

    private final MieszkanieService mieszkanieService;

    public MieszkanieController(MieszkanieService mieszkanieService) {
        this.mieszkanieService = mieszkanieService;
    }

    @PostMapping
    public ResponseEntity<MieszkanieDTO> createMieszkanie(@RequestBody MieszkanieDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MieszkanieDTO created = mieszkanieService.createMieszkanie(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MieszkanieDTO> getMieszkanie(@PathVariable Long id) {
        MieszkanieDTO dto = mieszkanieService.getMieszkanieById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<MieszkanieDTO>> getAllApproved() {
        List<MieszkanieDTO> mieszkania = mieszkanieService.getAllApproved();
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/my-listings")
    public ResponseEntity<List<MieszkanieDTO>> getMyListings() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MieszkanieDTO> mieszkania = mieszkanieService.getUserMieszkania(userId);
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/filter/city")
    public ResponseEntity<List<MieszkanieDTO>> filterByCity(@RequestParam String city) {
        List<MieszkanieDTO> mieszkania = mieszkanieService.filterByCity(city);
        return ResponseEntity.ok(mieszkania);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<MieszkanieDTO>> filterByPrice(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<MieszkanieDTO> mieszkania = mieszkanieService.filterByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(mieszkania);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MieszkanieDTO> updateMieszkanie(
            @PathVariable Long id,
            @RequestBody MieszkanieDTO dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MieszkanieDTO updated = mieszkanieService.updateMieszkanie(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMieszkanie(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mieszkanieService.deleteMieszkanie(id, userId);
        return ResponseEntity.ok("Mieszkanie usunieto pomyslnie");
    }

    // tylko ADMIN
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveMieszkanie(@PathVariable Long id) {
        mieszkanieService.approveMieszkanie(id);
        return ResponseEntity.ok("Mieszkanie zatwierdzone");
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectMieszkanie(@PathVariable Long id) {
        mieszkanieService.rejectMieszkanie(id);
        return ResponseEntity.ok("Mieszkanie odrzucone");
    }
}