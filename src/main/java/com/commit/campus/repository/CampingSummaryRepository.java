package com.commit.campus.repository;

import com.commit.campus.entity.CampingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampingSummaryRepository extends JpaRepository<CampingSummary, Long> {
}
