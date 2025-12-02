package com.example.RentApartments.dto;

import java.time.LocalDate;

public class RezerwacjaDTO {
    private Long id;
    private Long userId;
    private Long mieszkanieId;
    private LocalDate data_od;
    private LocalDate data_do;
    private String status; // PENDING, CONFIRMED, CANCELLED

    public RezerwacjaDTO() {}

    public RezerwacjaDTO(Long id, Long userId, Long mieszkanieId, LocalDate od, LocalDate do_date, String status) {
        this.id = id;
        this.userId = userId;
        this.mieszkanieId = mieszkanieId;
        this.data_od = od;
        this.data_do = do_date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMieszkianieId() {
        return mieszkanieId;
    }

    public void setMieszkianieId(Long mieszkanieId) {
        this.mieszkanieId = mieszkanieId;
    }

    public LocalDate getData_od() {
        return data_od;
    }

    public void setData_od(LocalDate data_od) {
        this.data_od = data_od;
    }

    public LocalDate getData_do() {
        return data_do;
    }

    public void setData_do(LocalDate data_do) {
        this.data_do = data_do;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}