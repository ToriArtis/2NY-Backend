package com.mega._NY.auth.repository;

import com.mega._NY.auth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName( String nickName);
    Optional<User> findByPhone( String phoneNumber);
    Optional<User> findUserByEmailAndProvider(String email, String provider);

    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    boolean existsByPhone(String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    int updatePassword(@Param("email") String email, @Param("password") String password);

}
