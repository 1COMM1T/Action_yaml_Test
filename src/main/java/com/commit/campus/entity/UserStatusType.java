package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="user_status_type")
@Getter
@ToString
public class UserStatusType implements Serializable {

    @Id
    @Column(name = "status_type_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusTypeId;

    @Column(name = "status_type", nullable = false)
    private String statusType;


}
