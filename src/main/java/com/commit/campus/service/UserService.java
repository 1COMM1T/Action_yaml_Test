package com.commit.campus.service;

import com.commit.campus.dto.SignUpUserRequest;
import com.commit.campus.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findUserByEmail(String email);
    void signUpUser(SignUpUserRequest userRequest);
    void withdrawUser(Long userId);
}
