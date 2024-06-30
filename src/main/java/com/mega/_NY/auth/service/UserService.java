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
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;


    public User join(UserDTO.SignUpDTO userDTO) throws BusinessLogicException {
        String email = userDTO.getEmail();
        String nickName = userDTO.getNickName();
        String phone = userDTO.getPhone();

        if (userRepository.existsByPhone(phone)) {
            throw new BusinessLogicException(ExceptionCode.EXIST_PHONE_NUMBER);
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);
        }
        if (userRepository.existsByNickName(nickName)) {
            throw new BusinessLogicException(ExceptionCode.EXIST_NICK_NAME);
        }
//        if (nickName.length() > 5) {  // 물어보기!!!
//            throw new NickNameLengthException("닉네임은 5글자 이하여야 합니다.");
//        }

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //password는 암호화
        createRole(user);

        return userRepository.save(user);
    }
    
    // 로그인 로직
    public User getByCredentials(final String email, final String password) {
        // 주어진 이메일을 사용하여 사용자 정보를 데이터베이스에서 조회

        log.info("getByCredentials");
        final User onlineUser = userRepository.findByEmail(email).orElseThrow();

        // 사용자가 존재하고 비밀번호가 일치하는지 확인
        if (onlineUser != null && passwordEncoder.matches(password, onlineUser.getPassword())) {
            // 비밀번호가 일치하면 사용자 객체를 반환
            return onlineUser;
        }
        // 비밀번호가 일치하지 않으면 null 반환
        return null;
    }



    private User createRole( User user ){
        List<String> roles = authUtils.createRoles();
        user.setRoles(roles);
        return user;
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

}
