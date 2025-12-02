package com.example.RentApartments.service;

import com.example.RentApartments.dto.MieszkanieDTO;
import com.example.RentApartments.model.Mieszkanie;
import com.example.RentApartments.model.Adres;
import com.example.RentApartments.model.User;
import com.example.RentApartments.repository.jpa.MieszkanieRepository;
import com.example.RentApartments.repository.jpa.AdresRepository;
import com.example.RentApartments.repository.jpa.UserRepository;
import com.example.RentApartments.repository.jpa.RezerwacjaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MieszkanieService {

    private final MieszkanieRepository mieszkanieRepository;
    private final AdresRepository adresRepository;
    private final UserRepository userRepository;
    private final LogService logService;
    private final ZdjecieService zdjecieService;
    private final ChatService chatService;
    private final RezerwacjaService rezerwacjaService;
    private final RezerwacjaRepository rezerwacjaRepository;

    public MieszkanieService(MieszkanieRepository mieszkanieRepository,
                             AdresRepository adresRepository,
                             UserRepository userRepository,
                             LogService logService,
                             ZdjecieService zdjecieService,
                             ChatService chatService,
                             RezerwacjaService rezerwacjaService,
                             RezerwacjaRepository rezerwacjaRepository) {
        this.mieszkanieRepository = mieszkanieRepository;
        this.adresRepository = adresRepository;
        this.userRepository = userRepository;
        this.logService = logService;
        this.zdjecieService = zdjecieService;
        this.chatService = chatService;
        this.rezerwacjaService = rezerwacjaService;
        this.rezerwacjaRepository = rezerwacjaRepository;
    }

    public MieszkanieDTO createMieszkanie(MieszkanieDTO dto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));

        Adres adres = adresRepository.findById(dto.getAdresId())
                .orElseThrow(() -> new RuntimeException("Adres nie zostal znaleziony"));

        Mieszkanie mieszkanie = new Mieszkanie();
        mieszkanie.setTytul(dto.getTytul());
        mieszkanie.setOpis(dto.getOpis());
        mieszkanie.setCena_miesieczna(dto.getCena_miesieczna());
        mieszkanie.setPowierzchnia(dto.getPowierzchnia());
        mieszkanie.setLiczba_pokoi(dto.getLiczba_pokoi());
        mieszkanie.setOwner(owner);
        mieszkanie.setAdres(adres);

        Mieszkanie saved = mieszkanieRepository.save(mieszkanie);
        
        logService.logEvent(userId, "OGLOSZENIE_DODANE", 
            "Nowe ogłoszenie dodane: \"" + dto.getTytul() + "\", cena: " + dto.getCena_miesieczna() + " PLN/miesiąc, powierzchnia: " + dto.getPowierzchnia() + " m²");
        
        return mapToDTO(saved);
    }

    public MieszkanieDTO getMieszkanieById(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));
        return mapToDTO(mieszkanie);
    }

    public List<MieszkanieDTO> getAllApproved() {
        return mieszkanieRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> getUserMieszkania(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Uzytkownik nie zostal znaleziony"));
        
        return user.getMieszkania()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> filterByCity(String city) {
        return mieszkanieRepository.findByAdresMiasto(city)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MieszkanieDTO> filterByPriceRange(Double minPrice, Double maxPrice) {
        return mieszkanieRepository.findByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MieszkanieDTO updateMieszkanie(Long id, MieszkanieDTO dto, Long userId) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));

        if (!mieszkanie.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized - tylko wlasciciel moze edytowac");
        }

        mieszkanie.setTytul(dto.getTytul());
        mieszkanie.setOpis(dto.getOpis());
        mieszkanie.setCena_miesieczna(dto.getCena_miesieczna());
        mieszkanie.setPowierzchnia(dto.getPowierzchnia());
        mieszkanie.setLiczba_pokoi(dto.getLiczba_pokoi());

        Mieszkanie updated = mieszkanieRepository.save(mieszkanie);
        
        logService.logEvent(userId, "OGLOSZENIE_EDYTOWANE", 
            "Ogłoszenie ID: " + id + " (\"" + dto.getTytul() + "\") została edytowane");
        
        return mapToDTO(updated);
    }

    public void deleteMieszkanie(Long id, Long userId) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));

        if (!mieszkanie.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized - tylko wlasciciel moze usunac");
        }

        // Cascade delete: Usuń wszystkie zdjęcia dla tego mieszkania
        List<com.example.RentApartments.model.Zdjecie> zdjecia = zdjecieService.getMieszkaniePhotos(id);
        for (com.example.RentApartments.model.Zdjecie z : zdjecia) {
            zdjecieService.deletePhoto(z.getId(), userId);
        }

        // Cascade delete: Usuń wszystkie czaty dla tego mieszkania
        List<com.example.RentApartments.model.Chat> chats = chatService.getMieszkanieChats(id);
        for (com.example.RentApartments.model.Chat c : chats) {
            chatService.deleteChat(c.getId());
        }

        // Cascade delete: Usuń wszystkie rezerwacje dla tego mieszkania
        List<com.example.RentApartments.model.Rezerwacja> rezerwacje = rezerwacjaRepository.findByMieszkanieId(id);
        for (com.example.RentApartments.model.Rezerwacja r : rezerwacje) {
            rezerwacjaService.cancelRezerwacja(r.getId());
        }

        logService.logEvent(userId, "OGLOSZENIE_USUNIETE", 
            "Ogłoszenie ID: " + id + " (\"" + mieszkanie.getTytul() + "\") została usunięte wraz z powiązanymi zdjęciami, czatami i rezerwacjami");

        mieszkanieRepository.delete(mieszkanie);
    }

    public void approveMieszkanie(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));
        mieszkanie.setStatus("APPROVED");
        mieszkanieRepository.save(mieszkanie);
        
        logService.logEvent(mieszkanie.getOwner().getId(), "OGLOSZENIE_ZATWIERDZONE", 
            "Ogłoszenie ID: " + id + " (\"" + mieszkanie.getTytul() + "\") została zatwierdzone przez administratora");
    }

    public void rejectMieszkanie(Long id) {
        Mieszkanie mieszkanie = mieszkanieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mieszkanie nie zostalo znalezione"));
        mieszkanie.setStatus("REJECTED");
        
        logService.logEvent(mieszkanie.getOwner().getId(), "OGLOSZENIE_ODRZUCONE", 
            "Ogłoszenie ID: " + id + " (\"" + mieszkanie.getTytul() + "\") została odrzucone przez administratora");
        
        mieszkanieRepository.save(mieszkanie);
    }

    private MieszkanieDTO mapToDTO(Mieszkanie mieszkanie) {
        MieszkanieDTO dto = new MieszkanieDTO();
        dto.setId(mieszkanie.getId());
        dto.setTytul(mieszkanie.getTytul());
        dto.setOpis(mieszkanie.getOpis());
        dto.setCena_miesieczna(mieszkanie.getCena_miesieczna());
        dto.setPowierzchnia(mieszkanie.getPowierzchnia());
        dto.setLiczba_pokoi(mieszkanie.getLiczba_pokoi());
        dto.setOwnerId(mieszkanie.getOwner().getId());
        dto.setAdresId(mieszkanie.getAdres().getId());
        return dto;
    }
}