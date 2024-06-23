package com.mega._NY.auth.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.AuthUtils;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserStatus;
import com.mega._NY.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;

    public User joinUser( User user ){
        existEmail(user.getEmail());
        existDisplayName(user.getNickName());
        encodePassword(user);
        existPhoneNum(user.getPhone());
        createRole(user);
        userRepository.save(user);
        return user;
    }

    private User createRole( User user ){
        List<String> roles = authUtils.createRoles();
        user.setRoles(roles);
        return user;
    }

    private void existPhoneNum(String PhoneNum){
        Optional<User> user  = userRepository.findByPhone(PhoneNum);
        if(user.isPresent()) throw new RuntimeException();

    }

    private User encodePassword( User user ){
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        return user;
    }

    private void existDisplayName( String nickName ){
        Optional<User> user = userRepository.findByNickName(nickName);
        if(user.isPresent()) throw new BusinessLogicException(ExceptionCode.EXIST_DISPLAY_NAME);
    }

    private void existEmail( String email ){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);

    }

    public User getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        log.info("회원 이메일 = {}", name);
        Optional<User> user = userRepository.findByEmail(name);
        return user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    public Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Optional<User> user = userRepository.findByEmail(name);
        return user.get().getId();
    }

    public User deleteUser(){
        User loginUser = getLoginUser();
        loginUser.setUserStatus(UserStatus.USER_WITHDRAWAL);
        return loginUser;
    }

    public User updateUser( UserDTO.Post userDto ){
        User loginUser = getLoginUser();
        encodePassword(loginUser);
        loginUser.setAddress(userDto.getAddress());
        loginUser.setPhone(userDto.getPhone());
        loginUser.setRealName(userDto.getRealName());
        loginUser.setNickName(userDto.getNickName());
        return loginUser;
    }

    public User updateOAuthInfo( UserDTO.PostMoreInfo userDto ){
        User loginUser = getLoginUser();
        loginUser.setUserStatus(UserStatus.USER_ACTIVE);
        loginUser.setAddress(userDto.getAddress());
        loginUser.setRealName(userDto.getRealName());
        loginUser.setPhone(userDto.getPhone());
        loginUser.setNickName(userDto.getNickName());
        loginUser.setCreatedAt(LocalDateTime.now());
        return loginUser;
    }
}
