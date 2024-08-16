package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "camping")
@Getter
@Setter
@DynamicUpdate
@ToString
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Camping implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_id")
    private long campId;

    @Column(name = "content_id")
    private int contentId;

    @Column(name = "camp_name", length = 100)
    private String campName;

    @Column(name = "line_intro", length = 255)
    private String lineIntro;

    @Column(name = "intro", columnDefinition = "MEDIUMTEXT")
    private String intro;

    @Column(name = "do_name", length = 50)
    private String doName;

    @Column(name = "sigungu_name", length = 50)
    private String sigunguName;

    @Column(name = "post_code", length = 10)
    private String postCode;

    @Column(name = "feature_summary", columnDefinition = "MEDIUMTEXT")
    private String featureSummary;

    @Column(name = "induty", length = 50)
    private String induty;

    @Column(name = "addr", length = 100)
    private String addr;

    @Column(name = "addr_details", length = 100)
    private String addrDetails;

    @Column(name = "mapX")
    private double mapX;

    @Column(name = "mapY")
    private double mapY;

    @Column(name = "tel", length = 50)
    private String tel;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "staff_count")
    private int staffCount;

    @Column(name = "general_site_cnt")
    private Integer generalSiteCnt;

    @Column(name = "car_site_cnt")
    private Integer carSiteCnt;

    @Column(name = "glamping_site_cnt")
    private Integer glampingSiteCnt;

    @Column(name = "caravan_site_cnt")
    private Integer caravanSiteCnt;

    @Column(name = "personal_caravan_site_cnt")
    private Integer personalCaravanSiteCnt;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "support_facilities")
    private String supportFacilities;

    @Column(name = "outdoor_activities")
    private String outdoorActivities;

    @Column(name = "pet_access")
    private String petAccess;

    @Column(name = "rental_gear_list")
    private String rentalGearList;

    @Column(name = "operation_day")
    private String operationDay;

    @Column(name = "first_image_url")
    private String firstImageUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "camp_id", referencedColumnName = "camp_id", insertable = false, updatable = false)
    private CampingSummary campingSummary;

    @OneToMany(mappedBy = "campingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampingFacilities> campingFacilities;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}

