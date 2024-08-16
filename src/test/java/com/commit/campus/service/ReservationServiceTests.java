package com.commit.campus.service;

import com.commit.campus.dto.ReservationDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.entity.Reservation;
import com.commit.campus.repository.AvailabilityRepository;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.repository.ReservationRepository;
import com.commit.campus.service.impl.ReservationServiceImpl;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private CampingRepository campingRepository;

    @Mock
    private RedisCommands<String, String> redisCommands;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @Captor
    private ArgumentCaptor<Reservation> reservationCaptor;

    private String reservationId;
    private String lockKey;
    private String campLockKey;
    private String redisKey;

    private Map<String, String> reservationInfo;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        reservationId = "1234567890";
        lockKey = "lock:reservation:" + reservationId;
        redisKey = "reservationInfo:" + reservationId;
        campLockKey = "lock:camp:1000"; // 캠핑장 ID를 기반으로 한 락 키

        reservationInfo = new HashMap<>();
        reservationInfo.put("reservationId", reservationId);
        reservationInfo.put("userId", "1");
        reservationInfo.put("campId", "1000");
        reservationInfo.put("campFacsId", "3");
        reservationInfo.put("reservationDate", LocalDateTime.now().toString());
        reservationInfo.put("entryDate", LocalDate.now().toString());
        reservationInfo.put("leavingDate", LocalDate.now().plusDays(2).toString());
        reservationInfo.put("reservationStatus", "pending");
        reservationInfo.put("gearRentalStatus", "N");
        reservationInfo.put("campFacsType", "3");

        reservationDTO = new ReservationDTO();

        Camping camping = new Camping();
        camping.setCampId(1000L);
        camping.setGeneralSiteCnt(10);
        camping.setCarSiteCnt(5);
        camping.setGlampingSiteCnt(2);
        camping.setCaravanSiteCnt(3);

        lenient().when(campingRepository.findById(1000L)).thenReturn(Optional.of(camping));
    }

    @Test
    void confirmReservation_예약_확정_성공() {

        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.set(eq(campLockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(reservationInfo);

        ReservationDTO confirmedReservation = reservationServiceImpl.confirmReservation(reservationId);

        assertNotNull(confirmedReservation);
        assertEquals(reservationId, confirmedReservation.getReservationId().toString());
        assertEquals("confirmation", confirmedReservation.getReservationStatus());

        verify(redisCommands).hset(redisKey, "reservationStatus", "confirmation");
        verify(reservationRepository).save(reservationCaptor.capture());
        assertEquals("confirmation", reservationCaptor.getValue().getReservationStatus());
        verify(redisCommands).del(lockKey);
        verify(redisCommands).del(campLockKey);
    }

    @Test
    void confirmReservation_예약_존재_예외() {

        reservationInfo.put("reservationStatus", "confirmation");
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.set(eq(campLockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(reservationInfo);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationServiceImpl.confirmReservation(reservationId);
        });

        assertEquals("이미 확정된 예약입니다: " + reservationId, exception.getMessage());

        verify(redisCommands).del(lockKey);
        verify(redisCommands).del(campLockKey);
    }

    @Test
    void confirmReservation_예약_취소_예외() {
        // Arrange
        reservationInfo.put("reservationStatus", "cancelled");
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.set(eq(campLockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(reservationInfo);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationServiceImpl.confirmReservation(reservationId);
        });

        assertEquals("이미 취소된 예약입니다.", exception.getMessage());

        verify(redisCommands).del(lockKey);
        verify(redisCommands).del(campLockKey);
    }

    @Test
    void confirmReservation_만료된_예약_예외() {
        // Arrange
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(new HashMap<>());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationServiceImpl.confirmReservation(reservationId);
        });

        assertEquals("이미 만료되었거나 존재하지 않는 예약입니다.", exception.getMessage());

        verify(redisCommands).del(lockKey);
        // 여기서는 campLockKey가 설정되지 않으므로 확인할 필요가 없습니다.
    }

    @Test
    void confirmReservation_동시_요청_실패() {
        // Arrange
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn(null);

        // Act & Assert
        ConcurrentModificationException exception = assertThrows(ConcurrentModificationException.class, () -> {
            reservationServiceImpl.confirmReservation(reservationId);
        });

        assertEquals("해당 예약은 현재 처리 중입니다. 잠시 후 다시 시도해 주세요.", exception.getMessage());
    }

    @Test
    void cancelReservation_예약_취소_성공() {
        // Arrange
        reservationInfo.put("reservationStatus", "pending");
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.set(eq(campLockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(reservationInfo);
        when(reservationRepository.findById(Long.valueOf(reservationId))).thenReturn(Optional.of(Reservation.builder().reservationId(Long.valueOf(reservationId)).build()));

        // Act
        reservationServiceImpl.cancelReservation(reservationId);

        // Assert
        verify(redisCommands).hset(redisKey, "reservationStatus", "cancelled");
        verify(reservationRepository).save(reservationCaptor.capture());
        assertEquals("cancelled", reservationCaptor.getValue().getReservationStatus());
        verify(redisCommands).del(lockKey);
        verify(redisCommands).del(campLockKey);
    }

    @Test
    void cancelReservation_예약_취소_예외() {
        // Arrange
        reservationInfo.put("reservationStatus", "cancelled");
        when(redisCommands.set(eq(lockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.set(eq(campLockKey), eq("locked"), any())).thenReturn("OK");
        when(redisCommands.hgetall(redisKey)).thenReturn(reservationInfo);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationServiceImpl.cancelReservation(reservationId);
        });

        assertEquals("이미 취소된 예약입니다.", exception.getMessage());

        verify(redisCommands).del(lockKey);
        verify(redisCommands).del(campLockKey);
    }
}