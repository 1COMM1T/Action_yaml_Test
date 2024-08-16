package com.commit.campus.repository;

import com.commit.campus.entity.User;
import com.commit.campus.entity.UserStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusHistoryRepository extends JpaRepository<UserStatusHistory, Integer> {
    UserStatusHistory findTopByUserOrderByModifiedDateDesc(User updatedUser);
}
