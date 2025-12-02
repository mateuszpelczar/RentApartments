package com.example.RentApartments.service;

import com.example.RentApartments.model.Zdjecie;
import com.example.RentApartments.repository.mongo.ZdjecieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZdjecieService {

    private final ZdjecieRepository zdjecieRepository;

    public ZdjecieService(ZdjecieRepository zdjecieRepository) {
        this.zdjecieRepository = zdjecieRepository;
    }

    public Zdjecie uploadPhoto(Long mieszkanieId, String base64Data, String description) {
        Zdjecie zdjecie = new Zdjecie();
        zdjecie.setMieszkanie_id(mieszkanieId);
        zdjecie.setUrl(base64Data);
        zdjecie.setOpis(description);

        return zdjecieRepository.save(zdjecie);
    }

    public List<Zdjecie> getMieszkaniePhotos(Long mieszkanieId) {
        return zdjecieRepository.findByMieszkanie_id(mieszkanieId);
    }

    public Zdjecie getPhotoById(String id) {
        return zdjecieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zdjecie nie zostalo znalezione"));
    }

    public void deletePhoto(String id) {
        zdjecieRepository.deleteById(id);
    }
}