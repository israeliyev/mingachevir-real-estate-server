package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.entity.InputParameterValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputParameterValueDto {
    private Long id;
    private String code;
    private Boolean enabled;
    private Integer value;
    private Date createDate;
    private Date updateDate;
    private ParameterDto parameter; // Replacing parameterId with the actual object

    public InputParameterValueDto(InputParameterValue inputParameterValue) {
        this.id = inputParameterValue.getId();
        this.code = inputParameterValue.getCode();
        this.enabled = inputParameterValue.getEnabled();
        this.value = inputParameterValue.getValue();
        this.createDate = inputParameterValue.getCreateDate();
        this.updateDate = inputParameterValue.getUpdateDate();
        if (Objects.nonNull(inputParameterValue.getParameter()))
            this.parameter = new ParameterDto(inputParameterValue.getParameter());
    }
}
