package com.example.RentApartments.model;

import jakarta.persistence.*;


@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relacja wiele do 1 do User
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    // relacja wiele do 1 do Rola
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Rola role;

    public UserRole() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rola getRole() {
        return role;
    }

    public void setRole(Rola role) {
        this.role = role;
    }


}

