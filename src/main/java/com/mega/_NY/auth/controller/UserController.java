package com.mega._NY.auth.controller;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.dto.LoginDTO;
import com.mega._NY.auth.dto.ResponseDTO;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.TokenProvider;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @Autowired
    private CartService cartService;

    @PostMapping()
    public ResponseEntity<?> registerUser(@RequestBody UserDTO.ResponseDTO userDTO){
        try {
            //서비스를 이용해 리포지터리에 사용자 저장
            User registeredUser = userService.join(userDTO);
            UserDTO.ResponseDTO responseUserDTO = userDTO.builder()
                    .email(registeredUser.getEmail())
                    .realName(registeredUser.getRealName())
                    .password(registeredUser.getPassword())
                    .address(registeredUser.getAddress())
                    .phone(registeredUser.getPhone())
                    .detailAddress(registeredUser.getDetailAddress())
                    .build();
            cartService.createCart(registeredUser);
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
            final UserDTO.LoginDTO responseUserDTO = UserDTO.LoginDTO.builder()
                    .email(user.getEmail())
                    .token(token)
                    .nickName(user.getNickName())
                    .roleSet(user.getRoleSet() )
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }
        else{
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping()
    public ResponseEntity<?> info() {
        try {
            User loginUser = userService.getLoginUser();
            UserDTO.ResponseDTO userDTO = userService.info(loginUser.getEmail());
            return ResponseEntity.ok(userDTO);
        } catch (BusinessLogicException e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping()
    public ResponseEntity<?> modify(@RequestBody UserDTO.ResponseDTO userDTO) {
        try {
            userService.modify(userDTO);
            return ResponseEntity.ok(userDTO);
        } catch (BusinessLogicException e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteUser();
            return ResponseEntity.ok().body(true);
        } catch (BusinessLogicException e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/role")
    public ResponseEntity<?> roleModify(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        log.info("Received password: " + password);
        if ("123456789".equals(password)) {  // Note: This is still not secure
            try {
                userService.roleModify();
                return ResponseEntity.ok().body("Roles updated successfully");
            } catch (BusinessLogicException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder().error("Invalid admin passwordd").build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
        }
    }
    @PostMapping("/password")
    public ResponseEntity<Boolean> passwordModify(@RequestBody LoginDTO request) {
        User user = userService.getByCredentials(request.getEmail(), request.getPassword());

        if (user != null) {
            log.info("Password successfully");
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/passwordFind")
    public ResponseEntity<?> passwordFind(@RequestBody LoginDTO request) {
        Boolean isPasswordUpdate = userService.newpassword(request);

        if (isPasswordUpdate) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
