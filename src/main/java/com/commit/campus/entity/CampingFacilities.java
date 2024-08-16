package com.commit.campus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "camping_facilities")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFacilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_facs_id")
    private long campFacsId;

    @Column(name = "camp_id")
    private long campId;

    @Column(name = "facs_type_id")
    private int facsTypeId;

    @Column(name = "internal_facilities_list")
    private String internalFacilitiesList;

    @Column(name = "toilet_cnt")
    private int toiletCnt;

    @Column(name = "shower_room_cnt")
    private int showerRoomCnt;

    @Column(name = "sink_cnt")
    private int sinkCnt;

    @Column(name = "brazier_class")
    private String brazierClass;

    @Column(name = "personal_trailer_status")
    private String personalTrailerStatus;

    @Column(name = "personal_caravan_status")
    private String personalCaravanStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id", insertable = false, updatable = false)
    @JsonBackReference
    private Camping campingEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facs_type_id", insertable = false, updatable = false)
    private FacilityType facilityTypeEntity;
}

