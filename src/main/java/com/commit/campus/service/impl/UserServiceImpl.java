package com.commit.campus.service.impl;

import com.commit.campus.dto.SignUpUserRequest;
import com.commit.campus.entity.User;
import com.commit.campus.entity.UserStatusHistory;
import com.commit.campus.entity.UserStatusType;
import com.commit.campus.repository.UserRepository;
import com.commit.campus.repository.UserStatusHistoryRepository;
import com.commit.campus.repository.UserStatusTypeRepository;
import com.commit.campus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserStatusTypeRepository userStatusTypeRepository;
    private final UserStatusHistoryRepository userStatusHistoryRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserStatusTypeRepository userStatusTypeRepository, UserStatusHistoryRepository userStatusHistoryRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userStatusTypeRepository = userStatusTypeRepository;
        this.userStatusHistoryRepository = userStatusHistoryRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public void signUpUser(SignUpUserRequest userRequest) {

        LocalDateTime currentTime = LocalDateTime.now();
        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .nickname(userRequest.getNickname())
                .birthDay(userRequest.getBirthDay())
                .registrationDate(currentTime)
                .enrollDate(currentTime)
                .phoneNumber(userRequest.getPhoneNumber())
                .userAddr(userRequest.getUserAddr())
                .role("ROLE_USER")
                .status(1)
                .build();
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이메일을 가진 회원을 찾을 수 없습니다: " + email);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    @Override
    public void withdrawUser(Long userId) {

        LocalDateTime currentTime = LocalDateTime.now();
        UserStatusType userStatusType = userStatusTypeRepository.findById(2).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        UserStatusHistory userStatusHistory = UserStatusHistory.builder()
                .modifiedDate(currentTime)
                .userStatusType(userStatusType)
                .user(user)
                .build();

        userStatusHistoryRepository.save(userStatusHistory);

        User updateUser = User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .status(2)
                .build();

        userRepository.save(updateUser);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }
}
