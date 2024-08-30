package com.mega._NY.auth.config;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.entity.UserStatus;
import com.mega._NY.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        if (userRepository.count() == 1) {
            List<User> testUsers = Arrays.asList(
                    createUser("user1@test.com", "password1", "Address 1", "Detail 1", "Nick1", "Real1", "1111111111"),
                    createUser("user2@test.com", "password2", "Address 2", "Detail 2", "Nick2", "Real2", "2222222222"),
                    createUser("user3@test.com", "password3", "Address 3", "Detail 3", "Nick3", "Real3", "3333333333"),
                    createUser("user4@test.com", "password4", "Address 4", "Detail 4", "Nick4", "Real4", "4444444444"),
                    createUser("user5@test.com", "password5", "Address 5", "Detail 5", "Nick5", "Real5", "5555555555"),
                    createUser("user6@test.com", "password6", "Address 6", "Detail 6", "Nick6", "Real6", "6666666666"),
                    createUser("user7@test.com", "password7", "Address 7", "Detail 7", "Nick7", "Real7", "7777777777"),
                    createUser("user8@test.com", "password8", "Address 8", "Detail 8", "Nick8", "Real8", "8888888888"),
                    createUser("user9@test.com", "password9", "Address 9", "Detail 9", "Nick9", "Real9", "9999999999"),
                    createUser("user10@test.com", "password10", "Address 10", "Detail 10", "Nick10", "Real10", "1010101010")
            );

            userRepository.saveAll(testUsers);
            System.out.println("Test users have been initialized");
        }
    }

    private User createUser(String email, String password, String address, String detailAddress, String nickName, String realName, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAddress(address);
        user.setDetailAddress(detailAddress);
        user.setNickName(nickName);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setRoleSet(Collections.singleton(UserRoles.USER));
        user.setUserStatus(UserStatus.USER_ACTIVE);
        return user;
    }
}