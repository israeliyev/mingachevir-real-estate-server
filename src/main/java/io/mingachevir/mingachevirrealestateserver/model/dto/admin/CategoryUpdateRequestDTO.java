package io.mingachevir.mingachevirrealestateserver.model.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class CategoryUpdateRequestDTO {
    private List<MainCategoryDTO> mainCategoriesToUpdate;
    private List<SubCategoryDTO> subCategoriesToUpdate;
    private List<ParametersDTO> parametersToUpdate;
    private List<SelectiveParameterValueDTO> selectiveParametersToUpdate;
    private List<Long> mainCategoryIdsToDelete;
    private List<Long> subCategoryIdsToDelete;
    private List<Long> parameterIdsToDelete;
    private List<Long> selectiveParameterIdsToDelete;
}
