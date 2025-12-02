package com.example.RentApartments.repository.mongo;

import com.example.RentApartments.model.Zdjecie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ZdjecieRepository extends MongoRepository<Zdjecie,String> {
    @Query("{ 'mieszkanie_id': ?0 }")
    List<Zdjecie> findByMieszkanie_id(Long mieszkanieId);
}

