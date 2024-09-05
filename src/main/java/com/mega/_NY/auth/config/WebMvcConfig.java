package com.mega._NY.auth.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://www.test.com:3000",
                                "http://2ny-frontendv3-env.eba-mhpsr73c.ap-northeast-2.elasticbeanstalk.com",
                                "https://2ny-frontendv3-env.eba-mhpsr73c.ap-northeast-2.elasticbeanstalk.com",
                                "http://www.2ny.kro.kr",
                                "https://www.2ny.kro.kr")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

}
