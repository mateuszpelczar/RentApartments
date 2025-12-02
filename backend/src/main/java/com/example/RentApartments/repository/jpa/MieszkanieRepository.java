package com.example.RentApartments.repository.jpa;

import com.example.RentApartments.model.Mieszkanie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MieszkanieRepository extends JpaRepository<Mieszkanie, Long> {
    
    List<Mieszkanie> findByStatus(String status);
    
    List<Mieszkanie> findByOwnerId(Long ownerId);
    
    @Query("SELECT m FROM Mieszkanie m JOIN m.adres a WHERE a.miasto = :city AND m.status = 'APPROVED'")
    List<Mieszkanie> findByAdresMiasto(@Param("city") String city);
    
    @Query("SELECT m FROM Mieszkanie m WHERE m.cena_miesieczna BETWEEN :minPrice AND :maxPrice AND m.status = 'APPROVED'")
    List<Mieszkanie> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}