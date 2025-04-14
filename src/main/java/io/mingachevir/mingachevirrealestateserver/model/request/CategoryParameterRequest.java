package io.mingachevir.mingachevirrealestateserver.model.request;


import io.mingachevir.mingachevirrealestateserver.model.dto.MainCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.ParameterDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryParameterRequest {
    private List<MainCategoryDto> mainCategoriess;
    private List<ParameterDto> parameters;
}
