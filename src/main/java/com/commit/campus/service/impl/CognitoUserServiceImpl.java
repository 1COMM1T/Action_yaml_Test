package com.commit.campus.service.impl;

import com.commit.campus.service.CognitoUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;

import java.util.Collections;

@Service
public class CognitoUserServiceImpl implements CognitoUserService {

    private final CognitoIdentityProviderClient cognitoClient;

    public CognitoUserServiceImpl() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminGetUserRequest request = AdminGetUserRequest.builder()
                .userPoolId("ap-northeast-2_2qtl6ioip")
                .username(username)
                .build();

        AdminGetUserResponse response;
        try {
            response = cognitoClient.adminGetUser(request);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found in Cognito", e);
        }

        String email = response.userAttributes().stream()
                .filter(attr -> attr.name().equals("email"))
                .findFirst()
                .map(attr -> attr.value())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found for user " + username));

        return new org.springframework.security.core.userdetails.User(email, "", Collections.emptyList());
    }
}
