package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import lombok.Data;

@Data
public class MainCategoryDTO {
    private Long id;
    private String name;
    private String code;
    private Long tempId;

    public MainCategory toEntity() {
        MainCategory entity = new MainCategory();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCode(this.code);
        entity.setEnabled(true);
        entity.setIsDeleted(false);
        return entity;
    }
}
