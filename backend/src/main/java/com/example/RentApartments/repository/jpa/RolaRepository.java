package com.example.RentApartments.repository.jpa;

import com.example.RentApartments.model.Rola;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolaRepository extends JpaRepository<Rola,Long> {
    Optional<Rola> findByRolename(String rolename);
}
