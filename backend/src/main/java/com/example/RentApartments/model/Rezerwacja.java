package com.example.RentApartments.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rezerwacje")
public class Rezerwacja {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalDate data_od;

    @Column(nullable=false)
    private LocalDate data_do;


    private String status;


    //relacje wiele do 1 z Mieszkanie
    @ManyToOne(optional = false)
    @JoinColumn(name="mieszkanie_id")
    private Mieszkanie mieszkanie;

    //relacja wiele do 1 z User
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Rezerwacja(){}

    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public LocalDate getData_od(){return data_od;}

    public void setData_od(LocalDate data_od){this.data_od=data_od;}

    public LocalDate getData_do(){return data_do;}

    public void setData_do(LocalDate data_do){this.data_do=data_do;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public Mieszkanie getMieszkanie(){return mieszkanie;}

    public void setMieszkanie(Mieszkanie mieszkanie){this.mieszkanie=mieszkanie;}

    public User getUser(){return user;}

    public void setUser(User user){this.user=user;}


}
