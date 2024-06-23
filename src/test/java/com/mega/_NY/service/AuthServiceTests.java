package com.mega._NY.service;

import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    private User user = User.builder()
            .email("ok1@gmail.com")
            .password("12344").address("집주소입니다.")
            .nickName("닉네임").realName("채현영")
            .phone("010-3434-3434").build();;



    @Test
    void 회원가입_테스트() throws Exception {
        //when
        User user1 = userService.joinUser(user);
        //then
        System.out.println(user1.getCreatedAt());
        assertEquals(1L, user1.getId());
    }

    @Test
    void 중복된_이메일_테스트() throws Exception {
        //given
        userService.joinUser(user);
        //when
        UserDTO.Post userSignUpDto = UserDTO.Post.builder().email("test1@gmail.com").password("12344").address("집주소입니다.")
                .nickName("닉네임").realName("채현영").phone("010-3434-3434").build();
        User user1 = modelMapper.map(userSignUpDto, User.class);
        //then
        assertThrows(RuntimeException.class, () -> userService.joinUser(user1));
    }

    @Test
    void 중복된_닉네임_테스트() throws Exception {
        //given
        userService.joinUser(user);
        //when
        UserDTO.Post userSignUpDto = UserDTO.Post.builder().email("test2@gmail.com").password("12344")
                .address("집주소입니다.").nickName("닉네임").realName("채현영").phone("010-3434-3434").build();
        User user1 = modelMapper.map(userSignUpDto, User.class);
        //then
        assertThrows(RuntimeException.class, () -> userService.joinUser(user1));
    }

    @Test
    void 중복된_연락처_테스트() throws Exception {
        //given
        userService.joinUser(user);
        //when
        UserDTO.Post userSignUpDto = UserDTO.Post.builder().email("test2@gmail.com").password("12344")
                .address("집주소입니다.").nickName("닉네임").realName("채현영").phone("010-3434-3434").build();
        User user1 = modelMapper.map(userSignUpDto, User.class);
        //then
        assertThrows(RuntimeException.class, () -> userService.joinUser(user1));
    }

    @Test
    void 가입한_회원역할_user() throws Exception {
        //given
        User user1 = userService.joinUser(user);
        //then
        assertEquals("USER", user1.getRoles().get(0));
    }
}