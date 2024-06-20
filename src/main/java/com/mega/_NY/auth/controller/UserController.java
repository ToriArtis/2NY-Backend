package com.mega._NY.auth.controller;

import com.mega._NY.auth.config.UserMapperConfig;
import com.mega._NY.auth.dto.UserDTO;
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
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {
//    private final Logout logout;
    @Autowired
    private UserMapperConfig userMapperConfig;
    private final UserService userService;

    @PostMapping
    public ResponseEntity singUpUser( @Valid @RequestBody UserDTO.Post userDto ){

        log.info("signUpUser: {}", userDto);
        User user = userMapperConfig.dtoToUser(userDto);
        log.error("user = {}", user.getUserStatus());
        log.error("user = {}", user.getEmail());
        userService.joinUser(user);
        String response = "회원가입이 완료되었습니다.";
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/more-info")
    public ResponseEntity moreInfo( @Valid @RequestBody UserDTO.PostMoreInfo userDto, HttpServletResponse response ) throws IOException {
//        User user = userService.updateOAuthInfo(userDto);
//        userService.giveToken(user, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateInfo( @Valid @RequestBody UserDTO.Post userDto ){
        log.error("컨트롤러 진입");
        User user = userService.updateUser(userDto);
        UserDTO.Response userInfo = userMapperConfig.userToDto(user, HttpMethod.PATCH);
        return new ResponseEntity<>(userInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity getUserInfo(){
        User loginUser = userService.getLoginUser();
        if(loginUser.getProvider() != null){

        }
        UserDTO.Response userInfo = userMapperConfig.userToDto(loginUser, HttpMethod.GET);
        return new ResponseEntity<>(userInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("/logout")
    public String handleLogout( HttpServletRequest request ){
//        logout.doLogout(request);
        return "로그아웃이 되었습니다!";
    }

    @DeleteMapping
    public ResponseEntity deleteUser( HttpServletRequest request ){
        User user = userService.deleteUser();
//        logout.doLogout(request);
        return new ResponseEntity<>(user.getUserStatus().getStatus(), HttpStatus.ACCEPTED);
    }

}
