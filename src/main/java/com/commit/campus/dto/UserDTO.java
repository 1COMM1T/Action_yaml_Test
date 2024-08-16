package com.commit.campus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDay;
    private String phoneNumber;
    private String userAddr;
    private String profileImageUrl;
    private LocalDateTime enrollDate;
    private LocalDateTime registrationDate;
    private String role;
    private int status;

    @Builder
    public UserDTO(Long userId, String email, String password, String name, String nickname, String birthDay, String phoneNumber, String userAddr, String profileImageUrl, LocalDateTime enrollDate, LocalDateTime registrationDate, String role, int status) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.userAddr = userAddr;
        this.profileImageUrl = profileImageUrl;
        this.enrollDate = enrollDate;
        this.registrationDate = registrationDate;
        this.role = role;
        this.status = status;
    }
}
