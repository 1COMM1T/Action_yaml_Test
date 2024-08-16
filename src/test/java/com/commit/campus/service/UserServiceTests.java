package com.commit.campus.service;

import com.commit.campus.dto.SignUpUserRequest;
import com.commit.campus.entity.User;
import com.commit.campus.entity.UserStatusHistory;
import com.commit.campus.repository.UserStatusHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    private UserService userService;
    private UserStatusHistoryRepository userStatusHistoryRepository;

    @Autowired
    public UserServiceTests(UserService userService, UserStatusHistoryRepository userStatusHistoryRepository) {
        this.userService = userService;
        this.userStatusHistoryRepository = userStatusHistoryRepository;

    }

    @Test
    @Transactional
    void 회원탈퇴_테스트() {
        //given
        SignUpUserRequest signUpUserRequest = SignUpUserRequest.builder()
                .name("테스트")
                .email("test@naver.com")
                .nickname("별명")
                .password("test1234")
                .build();

        userService.signUpUser(signUpUserRequest);
        User user = userService.findUserByEmail("test@naver.com");

        //when
        userService.withdrawUser(user.getUserId());

        //then
        User updatedUser = userService.findUserByEmail(user.getEmail());
        Assertions.assertEquals(updatedUser.getStatus(), 2);

        UserStatusHistory history = userStatusHistoryRepository.findTopByUserOrderByModifiedDateDesc(updatedUser);
        Assertions.assertNotNull(history);
        Assertions.assertEquals(history.getUserStatusType().getStatusTypeId(),2);
    }


    @Test
    @Transactional
    void 회원가입_테스트() {
        //given
        SignUpUserRequest userRequest = SignUpUserRequest.builder()
                .email("testtest@naver.com")
                .password("test1234")
                .name("테스트")
                .nickname("별명")
                .build();

        //when
        userService.signUpUser(userRequest);

        //then
        Assertions.assertEquals(userRequest.getEmail(), userService.findUserByEmail("testtest@naver.com").getEmail());
    }
}