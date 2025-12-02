package com.example.RentApartments.dto;

public class MieszkanieDTO {
    private Long id;
    private String tytul;
    private String opis;
    private Double cena_miesieczna;
    private Double powierzchnia;
    private Integer liczba_pokoi;
    private Long ownerId;
    private Long adresId;
    private String status; // PENDING, APPROVED, REJECTED
    private String City;

    public MieszkanieDTO() {}

    public MieszkanieDTO(Long id, String tytul, String opis, Double cena, Double powierzchnia, 
                        Integer pokoi, Long ownerId, Long adresId, String status) {
        this.id = id;
        this.tytul = tytul;
        this.opis = opis;
        this.cena_miesieczna = cena;
        this.powierzchnia = powierzchnia;
        this.liczba_pokoi = pokoi;
        this.ownerId = ownerId;
        this.adresId = adresId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Double getCena_miesieczna() {
        return cena_miesieczna;
    }

    public void setCena_miesieczna(Double cena_miesieczna) {
        this.cena_miesieczna = cena_miesieczna;
    }

    public Double getPowierzchnia() {
        return powierzchnia;
    }

    public void setPowierzchnia(Double powierzchnia) {
        this.powierzchnia = powierzchnia;
    }

    public Integer getLiczba_pokoi() {
        return liczba_pokoi;
    }

    public void setLiczba_pokoi(Integer liczba_pokoi) {
        this.liczba_pokoi = liczba_pokoi;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getAdresId() {
        return adresId;
    }

    public void setAdresId(Long adresId) {
        this.adresId = adresId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}