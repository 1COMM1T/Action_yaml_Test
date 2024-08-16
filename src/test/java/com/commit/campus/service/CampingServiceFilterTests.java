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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CampingServiceFilterTests {

    @Mock
    private CampingRepository campingRepository;

    @InjectMocks
    private CampingServiceImpl campingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCampingsWithFilters() {
        // Given
        Camping camping1 = new Camping();
        camping1.setCampId(1L);
        camping1.setCampName("알파 캠프");
        camping1.setDoName("경기도");
        camping1.setGlampingSiteCnt(5);

        Camping camping2 = new Camping();
        camping2.setCampId(2L);
        camping2.setCampName("베타 캠프");
        camping2.setDoName("강원도");
        camping2.setGlampingSiteCnt(10);

        List<Camping> mockCampings = Arrays.asList(camping1, camping2);
        int offset = 0;
        int limit = 10;

        // When
        when(campingRepository.findCampings("경기도", null, 5, null, offset, limit)).thenReturn(mockCampings);

        // Then
        List<Camping> result = campingService.getCampings("경기도", null, 5, null, 0, 10, "campId", "asc");
        assertEquals(2, result.size());
        assertEquals("알파 캠프", result.get(0).getCampName());
        assertEquals("베타 캠프", result.get(1).getCampName());
    }

    @Test
    public void testGetCampingById() {
        // Given
        Camping camping = new Camping();
        camping.setCampId(1L);
        camping.setCampName("알파 캠프");

        // When
        when(campingRepository.findById(1L)).thenReturn(Optional.of(camping));

        // Then
        Optional<Camping> result = campingService.getCampingById(1L);
        assertTrue(result.isPresent());
        assertEquals("알파 캠프", result.get().getCampName());
    }
}
