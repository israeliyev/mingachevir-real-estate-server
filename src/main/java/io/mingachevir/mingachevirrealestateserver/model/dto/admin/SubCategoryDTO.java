package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import lombok.Data;

import java.util.Date;

@Data
public class SubCategoryDTO {
    private Long id;
    private String name;
    private String code;
    private Long mainCategoryId;
    private Long tempId;

    public SubCategory toEntity(SubCategory existingEntity) {
        SubCategory entity = existingEntity != null ? existingEntity : new SubCategory();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCode(this.code);
        entity.setEnabled(true);
        entity.setIsDeleted(false);
        entity.setCreateDate(entity.getId() == null ? new Date() : entity.getCreateDate());
        entity.setUpdateDate(new Date());
        if (this.mainCategoryId != null) {
            MainCategory mc = new MainCategory();
            mc.setId(this.mainCategoryId);
            entity.setMainCategory(mc);
        } else {
            entity.setMainCategory(null);
        }
// Preserve existing parameters if not explicitly updated
        return entity;
    }
}
