package com.commit.campus.dto;

import lombok.Data;

@Data
public class CampingFacilitiesDTO {
    private long campFacsId;
    private long campId;
    private int facsTypeId;
    private String internalFacilitiesList;
    private int toiletCnt;
    private int showerRoomCnt;
    private int sinkCnt;
    private String brazierClass;
    private String personalTrailerStatus;
    private String personalCaravanStatus;
}
