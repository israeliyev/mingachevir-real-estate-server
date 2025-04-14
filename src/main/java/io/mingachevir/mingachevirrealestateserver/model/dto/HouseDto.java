package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseActiveDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {
    private Long id;
    private String name;
    private Double price;
    private PriceType priceType;
    private String location;
    private String coverImage;
    private String houseVideo;
    private Date createDate;
    private String description;
    private Date updateDate;
    private BaseActiveDto mainCategory;
    private BaseActiveDto subCategory;
    private List<HouseImageDto> houseImages; // One-to-Many relationship
    private Set<SelectiveParameterValueDto> selectiveParameters;
    private List<InputParameterValueDto> inputParameters;

    public HouseDto(House house) {
        this.id = house.getId();
        this.name = house.getName();
        this.price = house.getPrice();
        this.priceType = house.getPriceType();
        this.location = house.getLocation();
        this.coverImage = house.getCoverImage();
        this.houseVideo = house.getHouseVideo();
        this.createDate = house.getCreateDate();
        this.updateDate = house.getUpdateDate();
        this.description = house.getDescription();
        mainCategory = BaseActiveDto.builder().name(house.getMainCategory().getName()).id(house.getMainCategory().getId()).enabled(house.getMainCategory().getEnabled()).build();
        subCategory = BaseActiveDto.builder().name(house.getSubCategory().getName()).id(house.getSubCategory().getId()).enabled(house.getSubCategory().getEnabled()).build();

        // Convert houseImages list to HouseImageDto list
        this.houseImages = house.getHouseImages().stream()
                .map(HouseImageDto::new)  // Assuming HouseImageDto constructor exists
                .collect(Collectors.toList());

        // Convert houseSelectiveParameterValues set to SelectiveParameterValueDto set
        this.selectiveParameters = house.getSelectiveParameterValues().stream()
                .map(SelectiveParameterValueDto::new)  // Assuming SelectiveParameterValueDto constructor exists
                .collect(Collectors.toSet());

        // Convert houseInputParameters list to InputParameterValueDto list
        this.inputParameters = house.getInputParameterValues().stream()
                .map(InputParameterValueDto::new)  // Assuming InputParameterValueDto constructor exists
                .collect(Collectors.toList());
    }
}
