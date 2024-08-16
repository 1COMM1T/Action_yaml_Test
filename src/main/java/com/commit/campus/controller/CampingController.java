package com.commit.campus.controller;

import com.commit.campus.dto.CampingDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.service.CampingService;
import com.commit.campus.view.CampingView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Camping API", description = "캠핑장 관련 엔드포인트")
public class CampingController {

    @Autowired
    private CampingService campingService;

    @GetMapping("/v1/campings")
    @Operation(summary = "캠핑장 리스트 조회", description = "특정 도와 시군구, 글램핑 및 카라반 사이트 유무에 따라 캠핑장 리스트를 페이지네이션과 정렬을 적용하여 조회합니다.")
    public List<CampingView> getCampings(
            @RequestParam(required = false) @Parameter(description = "지역1의 이름") String doName,
            @RequestParam(required = false) @Parameter(description = "지역2의 이름") String sigunguName,
            @RequestParam(required = false) @Parameter(description = "글램핑 개수") Integer glampingSiteCnt,
            @RequestParam(required = false) @Parameter(description = "카라반 개수") Integer caravanSiteCnt,
            @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
            @RequestParam(defaultValue = "campId") @Parameter(description = "정렬 필드", example = "campId") String sort,
            @RequestParam(defaultValue = "desc") @Parameter(description = "정렬 순서", example = "desc") String order) {

        List<Camping> campings = campingService.getCampings(doName, sigunguName, glampingSiteCnt, caravanSiteCnt, page, size, sort, order);
        return campings.stream()
                .map(CampingView::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/v1/campings/{id}")
    @Operation(summary = "캠핑장 단일 조회", description = "특정 ID의 캠핑장 상세 정보를 조회합니다.")
    public ResponseEntity<CampingView> getCampingById(
            @PathVariable @Parameter(description = "캠핑장 ID", required = true) Long id) {

        Optional<Camping> camping = campingService.getCampingById(id);
        return camping.map(value -> ResponseEntity.ok(new CampingView(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/sortedByBookmarks")
    @Operation(summary = "찜한 수로 정렬된 캠핑장 리스트 조회", description = "찜한 수에 따라 정렬된 캠핑장 리스트를 조회합니다.")
    public ResponseEntity<List<CampingView>> getCampingsSortedByBookmarks() {
        List<CampingDTO> campings = campingService.getAllCampingsSortedByBookmarks();
        List<CampingView> viewModels = campings.stream()
                .map(CampingView::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(viewModels);
    }

    @GetMapping("/sortedByReviews")
    @Operation(summary = "리뷰 수로 정렬된 캠핑장 리스트 조회", description = "리뷰 수에 따라 정렬된 캠핑장 리스트를 조회합니다.")
    public ResponseEntity<List<CampingView>> getCampingsSortedByReviews() {
        List<CampingDTO> campings = campingService.getAllCampingsSortedByReviews();
        List<CampingView> viewModels = campings.stream()
                .map(CampingView::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(viewModels);
    }
}


