package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.Data;

import java.util.Date;

@Data
public class HouseCardDto {
    private Long id;
    private String name;
    private Double price;
    private PriceType priceType;
    private String location;
    private String coverImage;
    private Date createDate;

    public HouseCardDto(House house) {
        this.id = house.getId();
        this.name = house.getName();
        this.price = house.getPrice();
        this.priceType = house.getPriceType();
        this.location = house.getLocation();
        this.coverImage = house.getCoverImage();
        this.createDate = house.getCreateDate();
    }
}
