package com.mega._NY.auth.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/")
public class TestContoller {
    
     @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("서버 정상 작동중!");
    }

}
