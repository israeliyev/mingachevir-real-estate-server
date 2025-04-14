package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectiveParameterValueDto {
    private Long id;
    private Boolean enabled;
    private String name;
    private Date createDate;
    private Date updateDate;
    private ParameterDto parameter; // Replacing parameterId with the actual object

    public SelectiveParameterValueDto(SelectiveParameterValue selectiveParameter) {
        this.id = selectiveParameter.getId();
        this.name = selectiveParameter.getName();
        this.enabled = selectiveParameter.getEnabled();
        this.createDate = selectiveParameter.getCreateDate();
        this.updateDate = selectiveParameter.getUpdateDate();
        if(Objects.nonNull(selectiveParameter.getParameter()))
            this.parameter = new ParameterDto(selectiveParameter.getParameter());
    }
}
