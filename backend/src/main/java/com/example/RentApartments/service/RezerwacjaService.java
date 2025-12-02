package com.example.RentApartments.service;

import com.example.RentApartments.dto.RezerwacjaDTO;
import com.example.RentApartments.model.Rezerwacja;
import com.example.RentApartments.model.User;
import com.example.RentApartments.model.Mieszkanie;
import com.example.RentApartments.repository.jpa.RezerwacjaRepository;
import com.example.RentApartments.repository.jpa.UserRepository;
import com.example.RentApartments.repository.jpa.MieszkanieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RezerwacjaService {

  private final RezerwacjaRepository rezerwacjaRepository;
  private final UserRepository userRepository;
  private final MieszkanieRepository mieszkanieRepository;
  private final LogService logService;

  public RezerwacjaService(RezerwacjaRepository rezerwacjaRepository, UserRepository userRepository, MieszkanieRepository mieszkanieRepository, LogService logService){
    this.rezerwacjaRepository = rezerwacjaRepository;
    this.userRepository = userRepository;
    this.mieszkanieRepository = mieszkanieRepository;
    this.logService = logService;
  }

  public RezerwacjaDTO createRezerwacja(RezerwacjaDTO dto, Long userId){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));

    

    Mieszkanie mieszkanie = mieszkanieRepository.findById(dto.getMieszkianieId())
        .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));

    

    Rezerwacja rezerwacja = new Rezerwacja();
    rezerwacja.setUser(user);
    rezerwacja.setMieszkanie(mieszkanie);
    rezerwacja.setData_od(dto.getData_od());
    rezerwacja.setData_do(dto.getData_do());
    rezerwacja.setStatus("PENDING");

    Rezerwacja saved = rezerwacjaRepository.save(rezerwacja);
    
    logService.logEvent(userId, "REZERWACJA_UTWORZONA", 
        "Rezerwacja utworzona dla mieszkania ID: " + dto.getMieszkianieId() + ", od dnia: " + dto.getData_od() + ", do dnia: " + dto.getData_do());
    
    return mapToDTO(saved);

  }

  public RezerwacjaDTO getRezerwacjaById(Long id) {
        Rezerwacja rezerwacja = rezerwacjaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezerwacja nie zostala znaleziona"));
        return mapToDTO(rezerwacja);
    }

    public List<RezerwacjaDTO> getUserRezerwacje(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));
        
        return user.getRezerwacje()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void confirmRezerwacja(Long id) {
        Rezerwacja rezerwacja = rezerwacjaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezerwacja nie zostala znaleziona"));
        rezerwacja.setStatus("CONFIRMED");
        rezerwacjaRepository.save(rezerwacja);
        
        logService.logEvent(rezerwacja.getUser().getId(), "REZERWACJA_POTWIERDZONA", 
            "Rezerwacja ID: " + id + " została potwierdzona dla mieszkania ID: " + rezerwacja.getMieszkanie().getId());
    }

    public void cancelRezerwacja(Long id) {
        Rezerwacja rezerwacja = rezerwacjaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezerwacja nie zostala znaleziona"));
        rezerwacja.setStatus("CANCELLED");
        rezerwacjaRepository.save(rezerwacja);
        
        logService.logEvent(rezerwacja.getUser().getId(), "REZERWACJA_ANULOWANA", 
            "Rezerwacja ID: " + id + " została anulowana dla mieszkania ID: " + rezerwacja.getMieszkanie().getId());
    }

    private RezerwacjaDTO mapToDTO(Rezerwacja rezerwacja) {
        return new RezerwacjaDTO(
            rezerwacja.getId(),
            rezerwacja.getUser().getId(),
            rezerwacja.getMieszkanie().getId(),
            rezerwacja.getData_od(),
            rezerwacja.getData_do(),
            rezerwacja.getStatus()
        );
    }





  
}
