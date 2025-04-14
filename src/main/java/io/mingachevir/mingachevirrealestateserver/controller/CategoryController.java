package io.mingachevir.mingachevirrealestateserver.controller;

import io.mingachevir.mingachevirrealestateserver.model.dto.MainCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.ParameterDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.SubCategoryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.ActiveCategoriesDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.CategoryUpdateRequestDTO;
import io.mingachevir.mingachevirrealestateserver.model.dto.admin.HouseDTO;
import io.mingachevir.mingachevirrealestateserver.model.request.CategoryParameterRequest;
import io.mingachevir.mingachevirrealestateserver.service.CategoryService;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public GenericResponse<List<MainCategoryDto>> getMainCategoriesAndSubCategories() {
        return categoryService.getMainCategoriesAndSubCategories();
    }

    @GetMapping("/parameters/{subCategoryId}")
    public GenericResponse<List<ParameterDto>> getParametersBySubCategoryIdAnStandart(@PathVariable("subCategoryId") Long subCategoryId) {
        return categoryService.getParametersBySubCategoryId(subCategoryId);
    }

    @GetMapping("/parameters-values-footer")
    public GenericResponse<List<ParameterDto>> getParametersAndValuesForFooter() {
        return categoryService.getParametersAndValuesForFooter();
    }

    @GetMapping("/categories/active")
    public GenericResponse<ActiveCategoriesDTO> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    @GetMapping("/auth/categories/check-houses")
    public GenericResponse<Long> checkCategoryParametersHouses(
            @RequestParam(value = "mainCategoryIds", required = false) List<Long> mainCategoryIds,
            @RequestParam(value = "subCategoryIds", required = false) List<Long> subCategoryIds,
            @RequestParam(value = "parameterIds", required = false) List<Long> parameterIds,
            @RequestParam(value = "selectiveParameterIds", required = false) List<Long> selectiveParameterIds) {
        return categoryService.checkCategoryParametersHouses(mainCategoryIds, subCategoryIds, parameterIds, selectiveParameterIds);
    }

    @PutMapping("/auth/categories/update")
    public GenericResponse<String> deleteOrUpdateCategoryAndParameters(@RequestBody CategoryUpdateRequestDTO request) {
        return categoryService.deleteOrUpdateCategoryAndParameters(request);
    }

    @GetMapping("/auth/categories/derelict-houses")
    public GenericResponse<List<HouseDTO>> getDerelictHouses() {
        return categoryService.getDerelictHouses();
    }

    @GetMapping("/sub-category/without-main-category")
    public GenericResponse<List<SubCategoryDto>> getSubCategoriesWithoutMainCategory() {
        return categoryService.getSubCategoriesWithoutMainCategory();
    }
}
