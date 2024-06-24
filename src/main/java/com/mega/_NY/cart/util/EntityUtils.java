package com.mega._NY.cart.util;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public class EntityUtils {

    // ID로 엔티티를 찾고, 없으면 예외를 던지는 유틸리티 메서드
    public static <T> T findVerifiedEntity(JpaRepository<T, Long> repository, Long id, ExceptionCode exceptionCode) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(exceptionCode));
    }

}
