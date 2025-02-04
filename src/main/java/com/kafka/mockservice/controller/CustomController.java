package com.kafka.mockservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/response")
public class CustomController {

    @GetMapping("/200")
    public ResponseEntity<String> getAPI(){
        return ResponseEntity.ok().body("Successful custom response");
    }

    @GetMapping("/500")
    public ResponseEntity<String> throwError(){
        return ResponseEntity.internalServerError().build();
    }

}
