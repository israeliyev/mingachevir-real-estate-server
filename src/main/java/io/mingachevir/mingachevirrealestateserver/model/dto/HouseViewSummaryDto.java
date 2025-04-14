package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseViewSummaryDto {
    private Long houseId;
    private String coverImage;
    private Double price;
    private PriceType priceType;
    private String name;
    private String location;
    private Long viewedCount;
}
