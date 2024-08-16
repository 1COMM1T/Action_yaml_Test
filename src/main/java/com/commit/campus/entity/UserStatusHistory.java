package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="user_status_history")
@Getter
@Builder
@ToString
@NoArgsConstructor
public class UserStatusHistory implements Serializable {

    @Id
    @Column(name = "history_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;  // 이력 ID

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;  // 수정 날짜

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 사용자 ID

    @ManyToOne
    @JoinColumn(name = "status_type_id")
    private UserStatusType userStatusType;  // 상태 유형 ID

    @Builder
    public UserStatusHistory(Long historyId, LocalDateTime modifiedDate, User user, UserStatusType userStatusType) {
        this.historyId = historyId;
        this.modifiedDate = modifiedDate;
        this.user = user;
        this.userStatusType = userStatusType;
    }
}
