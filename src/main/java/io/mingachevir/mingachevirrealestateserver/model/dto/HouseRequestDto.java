package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.HouseRequest;
import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequestDto {
    private Long id;
    private String requester;
    private Double price;
    private PriceType priceType;
    private String location;
    private String number;
    private Date createDate;
    private String coverImage;

    private BaseIdNameDto mainCategory;
    private BaseIdNameDto subCategory;
    private List<HouseImageDto> houseImages;
    private Set<SelectiveParameterValueDto> selectiveParameters;
    private List<InputParameterValueDto> inputParameters;

    public HouseRequestDto(HouseRequest houseRequest) {
        this.id = houseRequest.getId();
        this.requester = houseRequest.getRequester();
        this.price = houseRequest.getPrice();
        this.priceType = houseRequest.getPriceType();
        this.location = houseRequest.getLocation();
        this.createDate = houseRequest.getCreateDate();
        this.number = houseRequest.getNumber();
        this.coverImage = houseRequest.getCoverImage();

        if (houseRequest.getMainCategory() != null && Objects.nonNull(houseRequest.getMainCategory().getId()) && houseRequest.getMainCategory().getId() != 0) {
            mainCategory = BaseIdNameDto.builder().name(houseRequest.getMainCategory().getName()).id(houseRequest.getMainCategory().getId()).build();
        }

        if (houseRequest.getSubCategory() != null && Objects.nonNull(houseRequest.getSubCategory().getId()) && houseRequest.getSubCategory().getId() != 0) {
            subCategory = BaseIdNameDto.builder().name(houseRequest.getSubCategory().getName()).id(houseRequest.getSubCategory().getId()).build();
        }

        if(!houseRequest.getHouseImages().isEmpty()) {
            // Convert houseImages list to HouseImageDto list
            this.houseImages = houseRequest.getHouseImages().stream()
                    .map(HouseImageDto::new)  // Assuming HouseImageDto constructor exists
                    .collect(Collectors.toList());
        }

        if(!houseRequest.getSelectiveParameterValues().isEmpty()) {
            // Convert houseSelectiveParameterValues set to SelectiveParameterValueDto set
            this.selectiveParameters = houseRequest.getSelectiveParameterValues().stream()
                    .map(SelectiveParameterValueDto::new)  // Assuming SelectiveParameterValueDto constructor exists
                    .collect(Collectors.toSet());
        }

        if(!houseRequest.getInputParameterValues().isEmpty()) {
            // Convert houseInputParameters list to InputParameterValueDto list
            this.inputParameters = houseRequest.getInputParameterValues().stream()
                    .map(InputParameterValueDto::new)  // Assuming InputParameterValueDto constructor exists
                    .collect(Collectors.toList());
        }
    }
}
