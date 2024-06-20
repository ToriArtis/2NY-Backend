package com.mega._NY.auth.repository;

import com.mega._NY.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName( String nickName);
    Optional<User> findByPhone( String phoneNumber);
}
