package com.commit.campus.dto;

import com.commit.campus.repository.CampingFacilitiesRepository;
import com.commit.campus.request.ReservationRequest;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationDTO {

    private Long reservationId;
    private Long userId;
    private Long campId;
    private Long campFacsId;
    private LocalDateTime reservationDate;
    private LocalDate entryDate;
    private LocalDate leavingDate;
    private String reservationStatus;
    private String gearRentalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer campFacsType;   // 예약한 시설 유형

    public static ReservationDTO mapToReservationDTO(ReservationRequest reservationRequest, CampingFacilitiesRepository campingFacilitiesRepository) {

        LocalDateTime reservationDate = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        long campFacsId = reservationRequest.getCampFacsId();
        int facsType = campingFacilitiesRepository.findById(campFacsId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid campFacsId: " + campFacsId))
                .getFacsTypeId();

        return ReservationDTO.builder()
                .reservationId(reservationRequest.getReservationId())
                .userId(Long.valueOf(reservationRequest.getUserId()))
                .campId(reservationRequest.getCampId())
                .campFacsId(reservationRequest.getCampFacsId())
                .reservationDate(reservationDate)
                .entryDate(LocalDate.parse(reservationRequest.getEntryDate(), dateTimeFormatter))
                .leavingDate(LocalDate.parse(reservationRequest.getLeavingDate(), dateTimeFormatter))
                .gearRentalStatus(reservationRequest.getGearRentalStatus())
                .campFacsType(facsType)
                .build();
    }
}
