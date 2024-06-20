package com.mega._NY.auth.config;

import com.mega._NY.auth.dto.UserSignUpDTO;
import com.mega._NY.auth.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapperConfig {
    User userSignUpDtoToUser( UserSignUpDTO userSignUpDto);

}
