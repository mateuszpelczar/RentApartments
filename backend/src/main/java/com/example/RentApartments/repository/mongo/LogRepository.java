package com.example.RentApartments.repository.mongo;


import com.example.RentApartments.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LogRepository extends MongoRepository<Log, String> {
    @Query("{ 'user_id': ?0 }")
    List<Log> findByUser_id(Long userId);
    
    @Query("{ 'akcja': ?0 }")
    List<Log> findByAkcja(String akcja);
}
