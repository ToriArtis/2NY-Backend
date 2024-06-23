package com.mega._NY.auth.controller;

import com.mega._NY.auth.config.logout.Logout;
import com.mega._NY.auth.dto.LoginDTO;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final JwtToken jwtUtil;
    private final AuthenticationManager authenticationManager;


    @PostMapping
    public ResponseEntity<?> singUpUser( @Valid @RequestBody UserDTO.Post userSignUpDto ){
        User user = modelMapper.map(userSignUpDto, User.class);
        log.info(user);
        userService.joinUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> handleLogout( HttpServletRequest request ){
        logout.doLogout(request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(){
        User user = userService.getLoginUser();
        UserDTO.Response userinfo = modelMapper.map(user, UserDTO.Response.class);
        log.info("getUserInfo" + userinfo);
        return new ResponseEntity<>(userinfo, HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity deleteUser( HttpServletRequest request ){
        User user = userService.deleteUser();
        logout.doLogout(request);
        return new ResponseEntity<>(user.getUserStatus().getStatus(), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity updateInfo( @Valid @RequestBody UserDTO.Post userDto ){
        log.error("컨트롤러 진입");
        User user = userService.updateUser(userDto);
        UserDTO.Response userInfo = modelMapper.map(user, UserDTO.Response.class);
        return new ResponseEntity(userInfo, HttpStatus.ACCEPTED);
    }
}
