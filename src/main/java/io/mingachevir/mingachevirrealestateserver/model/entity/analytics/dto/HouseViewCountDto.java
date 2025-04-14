package io.mingachevir.mingachevirrealestateserver.model.entity.analytics.dto;

import lombok.Data;

@Data
public class HouseViewCountDto {
    private Long houseId;
    private String houseName;
    private Long viewCount;

    // Constructor, Getters, and Setters
    public HouseViewCountDto(Long houseId, String houseName, Long viewCount) {
        this.houseId = houseId;
        this.houseName = houseName;
        this.viewCount = viewCount;
    }
    // Getters and Setters
}


