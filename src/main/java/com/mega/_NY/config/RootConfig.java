package com.mega._NY.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // 필드 매칭을 활성화합니다. 이를 통해 필드 이름을 기준으로 객체 간의 매핑이 이루어집니다.
                .setFieldMatchingEnabled(true)
                // 필드 접근 수준을 PRIVATE으로 설정합니다. 이를 통해 private 필드도 매핑할 수 있도록 합니다.
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                // 매칭 전략을 LOOSE로 설정합니다. 이를 통해 유연한 매핑이 가능해집니다.
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
