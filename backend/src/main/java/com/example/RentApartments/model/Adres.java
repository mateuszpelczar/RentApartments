package com.example.RentApartments.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "adresy")
public class Adres{


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String ulica;

    @Column(nullable=false)
    private String numer;

    @Column(nullable=false)
    private String kod_pocztowy;

    @Column(nullable=false)
    private String miasto;


    @OneToMany(mappedBy = "adres")
    private List<Mieszkanie> mieszkania = new ArrayList<>();

    public Adres(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUlica() {
        return ulica;
    }
    public void setUlica(String ulica) {
        this.ulica = ulica;
    }
    public String getNumer() {
        return numer;
    }
    public void setNumer(String numer) {
        this.numer = numer;
    }
    public String getKod_pocztowy() {
        return kod_pocztowy;
    }
    public void setKod_pocztowy(String kod_pocztowy) {
        this.kod_pocztowy = kod_pocztowy;
    }
    public String getMiasto() {
        return miasto;
    }
    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public List<Mieszkanie> getMieszkania() {
        return mieszkania;
    }

    public void setMieszkania(List<Mieszkanie> mieszkania) {
        this.mieszkania = mieszkania;
    }








}
