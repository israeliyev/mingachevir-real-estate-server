package io.mingachevir.mingachevirrealestateserver.model.dto;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.util.ParameterTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ParameterDto {
    private Long id;
    private String code;
    private String name;
    private Boolean enabled;
    private ParameterTypeEnum type;
    private Date createDate;
    private Date updateDate;
    private BaseIdNameDto subCategory; // Replacing subCategoryId with the actual object
    private BaseIdNameDto mainCategory; // Replacing subCategoryId with the actual object
    private List<BaseIdNameDto> selectiveParameterValues;
    private List<BaseIdNameDto> inputParameterValues;

    public ParameterDto(Parameters parameter) {
        if (parameter != null) {
            this.id = parameter.getId();
            this.code = parameter.getCode();
            this.name = parameter.getName();
            this.enabled = parameter.getEnabled();
            this.type = parameter.getType();
            this.createDate = parameter.getCreateDate();
            this.updateDate = parameter.getUpdateDate();
            if (Objects.nonNull(parameter.getSubCategory())) {
                this.subCategory = new BaseIdNameDto(parameter.getSubCategory().getId(), parameter.getSubCategory().getName());
            }

            if (Objects.nonNull(parameter.getMainCategory())) {
                this.mainCategory = new BaseIdNameDto(parameter.getMainCategory().getId(), parameter.getMainCategory().getName());

            }

            if (!parameter.getSelectiveParameterValues().isEmpty()) {
                selectiveParameterValues = new ArrayList<>();
                parameter.getSelectiveParameterValues().forEach(selectiveParameter -> {
                    selectiveParameterValues.add(BaseIdNameDto.builder().name(selectiveParameter.getName()).id(selectiveParameter.getId()).build());
                });
            } else {
                selectiveParameterValues = new ArrayList<>();
            }

            if (!parameter.getInputParameterValues().isEmpty()) {
                inputParameterValues = new ArrayList<>();
                parameter.getInputParameterValues().forEach(inputParameterValue -> {
                    inputParameterValues.add(BaseIdNameDto.builder().name(parameter.getName()).id(inputParameterValue.getId()).build());
                });
            } else {
                inputParameterValues = new ArrayList<>();
            }
        }
    }
}
