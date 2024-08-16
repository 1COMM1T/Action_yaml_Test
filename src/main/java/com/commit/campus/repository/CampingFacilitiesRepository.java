package com.commit.campus.repository;

import com.commit.campus.entity.CampingFacilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampingFacilitiesRepository extends JpaRepository<CampingFacilities, Long> {
}
