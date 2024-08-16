package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "camping_statistics")
@Getter
@NoArgsConstructor
@ToString
public class CampingStatistics {

    @Id
    @Column(name = "camp_id")
    private Long campId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "camp_id")
    private Camping camping;

    @Column(name = "bookmark_cnt", nullable = false)
    private int bookmarkCnt;

    @Column(name = "review_cnt", nullable = false)
    private int reviewCnt;


}
