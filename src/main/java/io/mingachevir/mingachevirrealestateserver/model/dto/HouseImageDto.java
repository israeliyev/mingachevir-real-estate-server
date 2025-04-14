package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.HouseImage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
public class HouseImageDto {
    private Long id;
    private String path;
    private String fileName;
    private Date createDate;
    private Date updateDate;

    public HouseImageDto(HouseImage houseImage) {
        if (Objects.nonNull(houseImage.getId())) {
            this.id = houseImage.getId();
            this.path = houseImage.getPath();
            this.fileName = houseImage.getFileName();
            this.createDate = houseImage.getCreateDate();
            this.updateDate = houseImage.getUpdateDate();
        }
    }
}
