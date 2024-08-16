package com.commit.campus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SignUpUserRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDay;
    private String phoneNumber;
    private String userAddr;

    @Builder
    public SignUpUserRequest(String email, String password, String name, String nickname, String birthDay, String phoneNumber, String userAddr) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.userAddr = userAddr;
    }
}
