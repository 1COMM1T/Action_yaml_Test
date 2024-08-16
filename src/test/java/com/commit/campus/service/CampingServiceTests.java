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
public class CampingServiceTests {

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

        Camping camping3 = new Camping();
        camping3.setCampId(3L);
        camping3.setCampName("감마 캠프");
        camping3.setDoName("경기도");
        camping3.setGlampingSiteCnt(7);

        Camping camping4 = new Camping();
        camping4.setCampId(4L);
        camping4.setCampName("델타 캠프");
        camping4.setDoName("경기도");
        camping4.setGlampingSiteCnt(3);

        Camping camping5 = new Camping();
        camping5.setCampId(5L);
        camping5.setCampName("엡실론 캠프");
        camping5.setDoName("경기도");
        camping5.setGlampingSiteCnt(5);

        List<Camping> mockCampings = Arrays.asList(camping1, camping2, camping3, camping4, camping5);
        int offset = 0;
        int limit = 10;

        // When
        when(campingRepository.findCampings("경기도", null, 5, null, offset, limit)).thenReturn(mockCampings);

        // Then
        List<Camping> result = campingService.getCampings("경기도", null, 5, null, 0, 10, "campId", "asc");
        assertEquals(5, result.size());
        assertEquals("알파 캠프", result.get(0).getCampName());
        assertEquals("베타 캠프", result.get(1).getCampName());
        assertEquals("감마 캠프", result.get(2).getCampName());
        assertEquals("델타 캠프", result.get(3).getCampName());
        assertEquals("엡실론 캠프", result.get(4).getCampName());
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
// 단일 조회 + 정렬 + 필터 까지 포함 테스트