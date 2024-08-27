package com.mega._NY.auth.controller;


import com.mega._NY.auth.dto.ChatGPTDTO;
import com.mega._NY.auth.service.ChatGPTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class TestContoller {
    
     @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("서버 정상 작동중!");
    }

}
