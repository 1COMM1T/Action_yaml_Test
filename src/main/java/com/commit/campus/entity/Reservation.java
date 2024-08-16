package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "reservation")
@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Serializable {
    @Id
    @Column(name = "reservation_id")
    private long reservationId;  // 예약 ID

    @Column(name = "user_id")
    private long userId;       // 유저 id

    @Column(name = "camp_id")
    private long campId;        // 예약한 캠핑장

    @Column(name = "camp_facs_id")
    private long campFacsId;    // 예약한 시설

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;  // 예약 날짜

    @Column(name = "entry_date")
    private LocalDate entryDate;  // 입실 날짜

    @Column(name = "leaving_date")
    private LocalDate leavingDate;  // 퇴실 날짜

    @Column(name = "reservation_status")
    private String reservationStatus;  // 예약 상태

    @Column(name = "gear_rental_status")
    private String gearRentalStatus;  // 장비 대여 상태

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // fk
    @ManyToOne
    @JoinColumn(name = "camp_facs_id", insertable = false, updatable = false)
    private CampingFacilities campingFacilities;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}
