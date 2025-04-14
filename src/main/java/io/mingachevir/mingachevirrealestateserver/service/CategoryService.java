package io.mingachevir.mingachevirrealestateserver.service;

import io.mingachevir.mingachevirrealestateserver.model.dto.MainCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.ParameterDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.SubCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.ActiveCategoriesDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.CategoryUpdateRequestDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.HouseDTO;
import io.mingachevir.mingachevirrealestateserver.model.request.CategoryParameterRequest;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    GenericResponse<List<MainCategoryDto>> getMainCategoriesAndSubCategories();

    GenericResponse<List<ParameterDto>> getParametersAndValuesForFooter();

    GenericResponse<List<ParameterDto>> getParametersBySubCategoryId(Long subCategoryId);

    GenericResponse<ActiveCategoriesDTO> getActiveCategories();

    GenericResponse<Long> checkCategoryParametersHouses(List<Long> mainCategoryIds,
                                                        List<Long> subCategoryIds,
                                                        List<Long> parameterIds,
                                                        List<Long> selectiveParameterIds);

    GenericResponse<String> deleteOrUpdateCategoryAndParameters(CategoryUpdateRequestDTO request);

    GenericResponse<List<HouseDTO>> getDerelictHouses();

    GenericResponse<List<SubCategoryDto>> getSubCategoriesWithoutMainCategory();
}
