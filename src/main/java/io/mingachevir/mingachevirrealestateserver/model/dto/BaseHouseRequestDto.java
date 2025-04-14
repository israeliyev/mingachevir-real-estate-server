package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.HouseRequest;
import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BaseHouseRequestDto {
    private Long id;
    private String requester;
    private Double price;
    private PriceType priceType;
    private String location;
    private String number;
    private Date createDate;
    private String coverImage;

    public BaseHouseRequestDto(HouseRequest houseRequest) {
        this.id = houseRequest.getId();
        this.requester = houseRequest.getRequester();
        this.price = houseRequest.getPrice();
        this.priceType = houseRequest.getPriceType();
        this.location = houseRequest.getLocation();
        this.createDate = houseRequest.getCreateDate();
        this.number = houseRequest.getRequester();
        this.coverImage = houseRequest.getCoverImage();
    }
}
