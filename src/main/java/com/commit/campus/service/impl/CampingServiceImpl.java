package com.commit.campus.service.impl;

import com.commit.campus.dto.BookmarkedCampingDTO;
import com.commit.campus.dto.CampingDTO;
import com.commit.campus.dto.CampingFacilitiesDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.entity.CampingFacilities;
import com.commit.campus.entity.CampingSummary;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.service.CampingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CampingServiceImpl implements CampingService {

    @Autowired
    private CampingRepository campingRepository;

    @Override
    public List<Camping> getAllCampings() {
        log.info("모든 캠핑장 정보를 조회합니다.");
        return campingRepository.findAll();
    }

    @Override
    public Camping createCamping(Camping camping) {
        log.info("새로운 캠핑장을 생성합니다: {}", camping.getCampName());
        return campingRepository.save(camping);
    }

    @Override
    public List<Camping> getCampings(String doName, String sigunguName, Integer glampingSiteCnt, Integer caravanSiteCnt, int page, int size, String sort, String order) {
        log.info("특정 조건으로 캠핑장 리스트를 조회합니다: 도명={}, 시군구명={}, 글램핑 사이트 수={}, 카라반 사이트 수={}, 페이지={}, 사이즈={}, 정렬={}, 순서={}",
                doName, sigunguName, glampingSiteCnt, caravanSiteCnt, page, size, sort, order);
        int offset = page * size;
        List<Camping> campings = campingRepository.findCampings(doName, sigunguName, glampingSiteCnt, caravanSiteCnt, offset, size);
        log.info("조회된 캠핑장 수: {}", campings.size());
        return campings.stream()
                .sorted(getComparator(sort, order))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Camping> getCampingById(Long campId) {
        log.info("캠핑장 ID로 단일 캠핑장을 조회합니다: campId={}", campId);
        return campingRepository.findById(campId);
    }

    @Override
    public CampingDTO toCampingDTO(Camping camping) {
        log.info("캠핑 엔티티를 DTO로 변환합니다: campId={}", camping.getCampId());
        CampingDTO dto = new CampingDTO();
        BeanUtils.copyProperties(camping, dto);
        List<CampingFacilitiesDTO> facilitiesDTOList = camping.getCampingFacilities().stream()
                .map(this::convertToFacilitiesDTO)
                .collect(Collectors.toList());
        dto.setCampingFacilities(facilitiesDTOList);

        CampingSummary campingSummary = camping.getCampingSummary();
        if (campingSummary != null) {
            log.info("캠핑 요약 정보를 DTO에 설정합니다: campId={}, bookmarkCnt={}, reviewCnt={}",
                    campingSummary.getCampId(), campingSummary.getBookmarkCnt(), campingSummary.getReviewCnt());
            dto.setBookmarkCnt(campingSummary.getBookmarkCnt());
            dto.setReviewCnt(campingSummary.getReviewCnt());
        } else {
            log.warn("캠핑 요약 정보가 없습니다: campId={}", camping.getCampId());
            dto.setBookmarkCnt(0);
            dto.setReviewCnt(0);
        }

        return dto;
    }

    @Override
    public List<CampingDTO> getAllCampingsSortedByBookmarks() {
        log.info("찜한 수로 정렬된 캠핑장 리스트를 조회합니다.");
        List<Camping> campings = campingRepository.findAllOrderByBookmarkCntDesc();
        log.info("조회된 캠핑장 수: {}", campings.size());
        return campings.stream()
                .map(this::toCampingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampingDTO> getAllCampingsSortedByReviews() {
        log.info("리뷰 개수로 정렬된 캠핑장 리스트를 조회합니다.");
        List<Camping> campings = campingRepository.findAllOrderByReviewCntDesc();
        log.info("조회된 캠핑장 수: {}", campings.size());
        return campings.stream()
                .map(this::toCampingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookmarkedCampingDTO getBookmarkedCamping(Long campId) {
        log.info("특정 캠핑장의 북마크 정보를 조회합니다: campId={}", campId);
        Camping camping = campingRepository.findById(campId).orElseThrow(() -> {
            log.error("유효하지 않은 캠핑장 ID: {}", campId);
            return new IllegalArgumentException("Invalid campId: " + campId);
        });
        return BookmarkedCampingDTO.builder()
                .campName(camping.getCampName())
                .doName(camping.getDoName())
                .sigunguName(camping.getSigunguName())
                .postCode(camping.getPostCode())
                .induty(camping.getInduty())
                .firstImageUrl(camping.getFirstImageUrl())
                .build();
    }

    private CampingFacilitiesDTO convertToFacilitiesDTO(CampingFacilities facilities) {
        CampingFacilitiesDTO dto = new CampingFacilitiesDTO();
        BeanUtils.copyProperties(facilities, dto);
        return dto;
    }

    private Comparator<Camping> getComparator(String sort, String order) {
        Comparator<Camping> comparator;

        switch (sort) {
            case "campName":
                comparator = Comparator.comparing(Camping::getCampName, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "createdDate":
                comparator = Comparator.comparing(Camping::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                comparator = Comparator.comparing(Camping::getCampId, Comparator.nullsLast(Comparator.naturalOrder()));
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
