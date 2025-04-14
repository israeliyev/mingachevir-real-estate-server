package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.Section;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SectionDto {
    private Long id;
    private String name;
    private String page;
    private Date createDate;
    private Date updateDate;
    List<HouseCardDto> houses;

    public SectionDto(Section section) {
        this.id = section.getId();
        this.name = section.getName();  // Assuming there's a name field in Section
        this.page = section.getPage();
        this.createDate = section.getCreateDate();
        this.updateDate = section.getUpdateDate();

        // Convert List of Houses to List of HouseDto
        this.houses = section.getHouses().stream()
                .map(HouseCardDto::new)
                .collect(Collectors.toList());
    }

}
