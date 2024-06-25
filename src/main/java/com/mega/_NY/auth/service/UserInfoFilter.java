package com.mega._NY.auth.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoFilter {
    private final UserRepository userRepository;

//    private void existDisplayName(String displayName) {
//        log.info("displayName = {}", displayName);
//        if (displayName == null) return;
//        Optional<User> user = userRepository.findByNickName(displayName);
//        if (user.isPresent()) throw new BusinessLogicException(ExceptionCode.EXIST_DISPLAY_NAME);
//    }

    public void existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);
    }

    private void existPhoneNum(String phoneNum) {
        log.info("phone = {}", phoneNum);
        if (phoneNum == null) return;
        Optional<User> user = userRepository.findByPhone(phoneNum);
        if (user.isPresent()) throw new BusinessLogicException(ExceptionCode.EXIST_PHONE_NUMBER);
    }

    public void filterUserInfo(User user) {
        existEmail(user.getEmail());
//        existDisplayName(user.getNickName());
        existPhoneNum(user.getPhone());
    }

//    public void filterMoreInfo(UserDTO.PostMoreInfo user) {
//        existDisplayName(user.getNickName());
//        existPhoneNum(user.getPhone());
//    }
//
//    public void filterUpdateUser(UserDTO.Post userDto) {
//        checkPhone(userDto);
//        checkDisplayName(userDto);
//    }
//
//    public void checkPhone(UserDTO.Post userDto) {
//        Optional<User> user = userRepository.findByNickName(userDto.getNickName());
//        if (user.isPresent()) {
//            verifiedMyPhone(userDto, user);
//        }
//    }
//
//    public void checkDisplayName(UserDTO.Post userDto) {
//        Optional<User> user = userRepository.findByNickName(userDto.getPhone());
//        if (user.isPresent()) {
//            verifiedMyDisplayName(userDto, user);
//        }
//    }
//
//    private void verifiedMyPhone(UserDTO.Post userDto, Optional<User> user) {
//        if (Objects.equals(user.get().getEmail(), userDto.getEmail())) return;
//        existDisplayName(userDto.getNickName());
//    }
//
//    private void verifiedMyDisplayName(UserDTO.Post userDto, Optional<User> user) {
//        if (Objects.equals(user.get().getEmail(), userDto.getEmail())) return;
//        existPhoneNum(userDto.getPhone());
//    }
}
