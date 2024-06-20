package com.mega._NY.auth.controller;

import com.mega._NY.auth.config.logout.Logout;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.dto.UserSignUpDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final Logout logout;
    private final ModelMapper modelMapper;
    private final UserService userService;


    @PostMapping
    public ResponseEntity singUpUser( @Valid @RequestBody UserSignUpDTO userSignUpDto ){
        User user = modelMapper.map(userSignUpDto, User.class);
        log.info(user);
        userService.joinUser(user);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public ResponseEntity handleLogout( HttpServletRequest request ){
        logout.doLogout(request);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }



}
