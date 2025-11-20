package com.example.RentApartments.repository.mongo;


import com.example.RentApartments.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {
}
