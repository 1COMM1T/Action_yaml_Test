package com.commit.campus.service;

import org.springframework.security.authentication.AuthenticationManager;

public interface OauthService {

    String getKakaoAccessToken(String code);
    void createKakaoUser(String token);
    void setAuthenticationManager(AuthenticationManager authenticationManager);
}
