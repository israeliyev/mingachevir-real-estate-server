package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import lombok.Data;

@Data
public class SelectiveParameterValueDTO {
    private Long id;
    private String name;
    private String code;
    private Long parameterId;
    private Long tempId;

    public SelectiveParameterValue toEntity() {
        SelectiveParameterValue entity = new SelectiveParameterValue();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setCode(this.code);
        entity.setEnabled(true);
        entity.setIsDeleted(false);
        if (this.parameterId != null) {
            Parameters param = new Parameters();
            param.setId(this.parameterId);
            entity.setParameter(param);
        }
        return entity;
    }
}
