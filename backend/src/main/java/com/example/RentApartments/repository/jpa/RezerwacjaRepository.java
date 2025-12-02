package com.example.RentApartments.repository.jpa;

import com.example.RentApartments.model.Rezerwacja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RezerwacjaRepository extends JpaRepository<Rezerwacja,Long> {

  List<Rezerwacja> findByUserId(Long userId);

  List<Rezerwacja> findByMieszkanieId(Long mieszkanieId);

  List<Rezerwacja> findByStatus(String status);
}
