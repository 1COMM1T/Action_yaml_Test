package com.commit.campus.repository;

import com.commit.campus.entity.Availability;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    @Query("SELECT a FROM Availability a WHERE a.campId = :campId AND a.date BETWEEN :startDate AND :endDate")
    List<Availability> findByCampIdAndDateBetween(@Param("campId") Long campId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM Availability a WHERE a.camp_id = :campId AND DATE(a.date) = DATE(:date)", nativeQuery = true)
    Availability findByCampIdAndDate(@Param("campId") Long campId, @Param("date") LocalDate date);
}
