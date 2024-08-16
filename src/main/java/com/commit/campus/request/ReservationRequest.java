package com.commit.campus.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReservationRequest {
    private long reservationId;
    private int userId;
    private long campId;
    private long campFacsId;
    private String entryDate;
    private String leavingDate;
    private String gearRentalStatus;
    private int campFacsType;
}
