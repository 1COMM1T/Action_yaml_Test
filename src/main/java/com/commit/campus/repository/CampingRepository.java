package com.commit.campus.repository;

import com.commit.campus.entity.Camping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampingRepository extends JpaRepository<Camping, Long> {

    @Query(value = "SELECT * FROM camping " +
            "WHERE (:doName IS NULL OR do_name = :doName) " +
            "AND (:sigunguName IS NULL OR sigungu_name = :sigunguName) " +
            "AND (:glampingSiteCnt IS NULL OR glamping_site_cnt >= :glampingSiteCnt) " +
            "AND (:caravanSiteCnt IS NULL OR caravan_site_cnt >= :caravanSiteCnt) " +
            "ORDER BY camp_id " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Camping> findCampings(@Param("doName") String doName,
                               @Param("sigunguName") String sigunguName,
                               @Param("glampingSiteCnt") Integer glampingSiteCnt,
                               @Param("caravanSiteCnt") Integer caravanSiteCnt,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM camping " +
            "WHERE (:doName IS NULL OR do_name = :doName) " +
            "AND (:sigunguName IS NULL OR sigungu_name = :sigunguName) " +
            "AND (:glampingSiteCnt IS NULL OR glamping_site_cnt >= :glampingSiteCnt) " +
            "AND (:caravanSiteCnt IS NULL OR caravan_site_cnt >= :caravanSiteCnt)", nativeQuery = true)
    long countCampings(@Param("doName") String doName,
                       @Param("sigunguName") String sigunguName,
                       @Param("glampingSiteCnt") Integer glampingSiteCnt,
                       @Param("caravanSiteCnt") Integer caravanSiteCnt);

    Page<Camping> findByCampIdIn(List<Long> reviewedCampIds, Pageable pageable);

    List<Camping> findByContentId(int i);

    // 찜한 수로 정렬된 캠핑장 리스트를 조회하는 쿼리
    @Query("SELECT c FROM Camping c JOIN FETCH c.campingSummary cs ORDER BY cs.bookmarkCnt DESC")
    List<Camping> findAllOrderByBookmarkCntDesc();

    // 리뷰 수로 정렬된 캠핑장 리스트를 조회하는 쿼리
    @Query("SELECT c FROM Camping c JOIN FETCH c.campingSummary cs ORDER BY cs.reviewCnt DESC")
    List<Camping> findAllOrderByReviewCntDesc();

}
