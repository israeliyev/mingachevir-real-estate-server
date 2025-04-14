package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class MainCategoryDto {
    private Long id;
    private String code;
    private String name;
    private Date createDate;
    private Date updateDate;
    private List<SubCategoryDto> subCategories; // One-to-Many relationship

    public MainCategoryDto(MainCategory mainCategory) {
        if (Objects.nonNull(mainCategory)) {
            this.id = mainCategory.getId();
            this.code = mainCategory.getCode();
            this.name = mainCategory.getName();
            this.createDate = mainCategory.getCreateDate();
            this.updateDate = mainCategory.getUpdateDate();

            // Convert subCategories if not null (Lazy fetching safety)
            if (mainCategory.getSubCategories() != null && !mainCategory.getSubCategories().isEmpty()) {
                this.subCategories = mainCategory.getSubCategories()
                        .stream()
                        .map(SubCategoryDto::new) // Convert each SubCategory to SubCategoryDto
                        .toList();
            }
        }
    }
}
