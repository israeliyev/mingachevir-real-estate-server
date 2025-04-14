package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import io.mingachevir.mingachevirrealestateserver.util.ParameterTypeEnum;
import lombok.Data;

@Data
public class ParametersDTO {
    private Long id;
    private String name;
    private String code;
    private ParameterTypeEnum type; // "input" or "select"
    private Long subCategoryId;
    private Long tempId;

    public Parameters toEntity() {
        Parameters entity = new Parameters();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCode(this.code);
        entity.setType(this.type);
        entity.setEnabled(true);
        entity.setIsDeleted(false);
        if (this.subCategoryId != null) {
            SubCategory sc = new SubCategory();
            sc.setId(this.subCategoryId);
            entity.setSubCategory(sc);
        }
        return entity;
    }
}
