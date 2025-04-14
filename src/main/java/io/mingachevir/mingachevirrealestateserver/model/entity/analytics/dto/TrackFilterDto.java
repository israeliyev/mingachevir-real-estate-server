package io.mingachevir.mingachevirrealestateserver.model.entity.analytics.dto;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackFilterDto {
    private Long count;
    private String searchQuery;
    private String mainCategoryName;
    private String subCategoryName;
    private List<BaseIdNameDto> parameterValues;
}
