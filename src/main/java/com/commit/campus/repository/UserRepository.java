package com.commit.campus.repository;

import com.commit.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    @Query(value = "SELECT nickname FROM user WHERE user_id = :userId", nativeQuery = true)
    String findNicknameByUserId(@Param("userId") long userId);
}
