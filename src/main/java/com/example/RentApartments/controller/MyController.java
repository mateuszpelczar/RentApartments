package com.example.RentApartments.controller;

import com.example.RentApartments.model.Log;
import com.example.RentApartments.model.User;
import com.example.RentApartments.service.MyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(myService.createUser(user));
    }

    @PostMapping("/logs")
    public ResponseEntity<Log> addLog(@RequestBody Log log) {
        return ResponseEntity.ok(myService.createLog(log));
    }
}
