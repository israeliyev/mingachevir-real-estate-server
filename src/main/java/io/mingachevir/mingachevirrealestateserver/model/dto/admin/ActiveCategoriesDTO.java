package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ActiveCategoriesDTO {
    private List<MainCategoryDTO> mainCategories;
    private List<SubCategoryDTO> subCategories;
    private List<ParametersDTO> parameters;
    private List<SelectiveParameterValueDTO> selectiveParameters;

    public ActiveCategoriesDTO(List<MainCategoryDTO> mainCategories, List<SubCategoryDTO> subCategories,
                               List<ParametersDTO> parameters, List<SelectiveParameterValueDTO> selectiveParameters) {
        this.mainCategories = mainCategories;
        this.subCategories = subCategories;
        this.parameters = parameters;
        this.selectiveParameters = selectiveParameters;
    }
}
