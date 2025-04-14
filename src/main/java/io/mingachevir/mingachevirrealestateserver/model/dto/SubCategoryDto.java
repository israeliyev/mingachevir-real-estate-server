package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class SubCategoryDto {
    private Long id;
    private String code;
    private String name;
    private Date createDate;
    private Date updateDate;
    private MainCategoryDto mainCategory;
    private List<ParameterDto> parameters;

    public SubCategoryDto(SubCategory subCategory) {
        if (Objects.nonNull(subCategory)) {
            this.id = subCategory.getId();
            this.code = subCategory.getCode();
            this.name = subCategory.getName();
            this.createDate = subCategory.getCreateDate();
            this.updateDate = subCategory.getUpdateDate();
        }
    }
}
