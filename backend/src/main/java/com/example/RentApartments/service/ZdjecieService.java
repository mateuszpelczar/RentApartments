package com.example.RentApartments.service;

import com.example.RentApartments.model.Zdjecie;
import com.example.RentApartments.repository.mongo.ZdjecieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZdjecieService {

    private final ZdjecieRepository zdjecieRepository;
    private final LogService logService;

    public ZdjecieService(ZdjecieRepository zdjecieRepository, LogService logService) {
        this.zdjecieRepository = zdjecieRepository;
        this.logService = logService;
    }

    public Zdjecie uploadPhoto(Long mieszkanieId, String base64Data, String description, Long userId) {
        Zdjecie zdjecie = new Zdjecie();
        zdjecie.setMieszkanie_id(mieszkanieId);
        zdjecie.setUrl(base64Data);
        zdjecie.setOpis(description);

        Zdjecie saved = zdjecieRepository.save(zdjecie);
        
        logService.logEvent(userId, "ZDJECIE_DODANE", 
            "Zdjęcie dodane do mieszkania ID: " + mieszkanieId + ", opis: " + description);
        
        return saved;
    }

    public List<Zdjecie> getMieszkaniePhotos(Long mieszkanieId) {
        return zdjecieRepository.findByMieszkanie_id(mieszkanieId);
    }

    public Zdjecie getPhotoById(String id) {
        return zdjecieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zdjecie nie zostalo znalezione"));
    }

    public void deletePhoto(String id, Long userId) {
        Zdjecie zdjecie = zdjecieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zdjecie nie zostalo znalezione"));
        
        zdjecieRepository.deleteById(id);
        
        logService.logEvent(userId, "ZDJECIE_USUNIETE", 
            "Zdjęcie ID: " + id + " zostało usunięte z mieszkania ID: " + zdjecie.getMieszkanie_id());
    }
}