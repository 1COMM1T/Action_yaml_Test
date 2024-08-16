package com.commit.campus.service;

import com.commit.campus.entity.Camping;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.service.impl.CampingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CampingServiceSortTests {

    @Mock
    private CampingRepository campingRepository;

    @InjectMocks
    private CampingServiceImpl campingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCampingsSortedByCampId() {
        // Given
        Camping camping1 = new Camping();
        camping1.setCampId(1L);
        camping1.setCampName("알파 캠프");

        Camping camping2 = new Camping();
        camping2.setCampId(2L);
        camping2.setCampName("베타 캠프");

        Camping camping3 = new Camping();
        camping3.setCampId(3L);
        camping3.setCampName("감마 캠프");

        List<Camping> mockCampings = Arrays.asList(camping3, camping1, camping2); // Unsorted list

        // When
        when(campingRepository.findCampings(null, null, null, null, 0, 10)).thenReturn(mockCampings);

        // Then
        List<Camping> result = campingService.getCampings(null, null, null, null, 0, 10, "campId", "asc");
        assertTrue(result.get(0).getCampId() < result.get(result.size() - 1).getCampId());
    }

    @Test
    public void testGetCampingsSortedByCreatedDate() {
        // Given
        Camping camping1 = new Camping();
        camping1.setCampId(1L);
        camping1.setCampName("알파 캠프");
        camping1.setCreatedDate(LocalDateTime.of(2022, 1, 1, 0, 0));

        Camping camping2 = new Camping();
        camping2.setCampId(2L);
        camping2.setCampName("베타 캠프");
        camping2.setCreatedDate(LocalDateTime.of(2022, 1, 2, 0, 0));

        Camping camping3 = new Camping();
        camping3.setCampId(3L);
        camping3.setCampName("감마 캠프");
        camping3.setCreatedDate(LocalDateTime.of(2022, 1, 3, 0, 0));

        List<Camping> mockCampings = Arrays.asList(camping2, camping3, camping1); // Unsorted list

        // When
        when(campingRepository.findCampings(null, null, null, null, 0, 10)).thenReturn(mockCampings);

        // Then
        List<Camping> result = campingService.getCampings(null, null, null, null, 0, 10, "createdDate", "asc");
        assertTrue(result.get(0).getCreatedDate().isBefore(result.get(result.size() - 1).getCreatedDate()));
    }
}
