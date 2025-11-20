package com.example.RentApartments.repository.jpa;


import com.example.RentApartments.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
