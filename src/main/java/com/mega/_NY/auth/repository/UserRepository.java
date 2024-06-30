package com.mega._NY.auth.repository;

import com.mega._NY.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName( String nickName);
    Optional<User> findByPhone( String phoneNumber);

    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    boolean existsByPhone(String phoneNumber);

    User findByEmailAndPassword(String email, String password);


}
