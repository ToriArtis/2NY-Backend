package com.mega._NY.auth.controller;

import com.mega._NY.auth.dto.LoginDTO;
import com.mega._NY.auth.dto.ResponseDTO;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.TokenProvider;
import com.mega._NY.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try {
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .realName(userDTO.getRealName())
                    .password(userDTO.getPassword())
                    .build();
            //서비스를 이용해 리포지터리에 사용자 저장
            User registeredUser = userService.joinUser(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .realName(registeredUser.getRealName())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/login")
    public  ResponseEntity<?> authenticate(@RequestBody LoginDTO userDTO){
        User user = userService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getPassword()
        );
        if( user != null){
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }
        else{
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed").build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }
}
