package com.commit.campus.service;

import com.commit.campus.dto.ReservationDTO;

public interface ReservationService {
    String createReservation(ReservationDTO reservationDTO);
    ReservationDTO confirmReservation(String reservationId);
    void cancelReservation(String reservationId);
}
