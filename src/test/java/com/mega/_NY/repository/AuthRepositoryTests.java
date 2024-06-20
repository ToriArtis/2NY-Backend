package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootTest
public class AuthRepositoryTests {

    @Autowired
    private UserRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUsers() {
        IntStream.range(50,55).forEach(i -> {
            User auth = User.builder()
                    .password(passwordEncoder.encode("1111"))
                    .nickName("tu" + i)
                    .email("test" + i + "@naver.com")
                    .provider("naver")
                    .address("address")
                    .detailAddress("detailAddress")
                    .phone("010" + i)
                    .providerId("id")
                    .realName("채현영" + i)
                    .roles(Arrays.asList("USER")) // 기본 역할 설정
                    .build();

            authRepository.save(auth);
        });
    }



}
