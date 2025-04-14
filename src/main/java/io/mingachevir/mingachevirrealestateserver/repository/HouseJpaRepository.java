package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HouseJpaRepository extends JpaRepository<House, Long>, JpaSpecificationExecutor<House> {
    House findByIdAndEnabledIsTrueAndIsDeletedIsFalse(Long houseId);


    @Query("SELECT COUNT(DISTINCT h) FROM House h " +
            "WHERE (:mainCategoryIds IS NULL OR h.mainCategory.id IN :mainCategoryIds) " +
            "OR (:subCategoryIds IS NULL OR h.subCategory.id IN :subCategoryIds) " +
            "OR EXISTS (SELECT 1 FROM h.inputParameterValues ipv WHERE ipv.parameter.id IN :parameterIds) " +
            "OR EXISTS (SELECT 1 FROM h.selectiveParameterValues spv WHERE spv.id IN :selectiveParameterIds)" +
            "OR EXISTS (SELECT 1 FROM h.selectiveParameterValues spv WHERE spv.parameter.id IN :parameterIds)"
    )
    Long countDistinctHousesByCategoriesAndParameters(
            @Param("mainCategoryIds") List<Long> mainCategoryIds,
            @Param("subCategoryIds") List<Long> subCategoryIds,
            @Param("parameterIds") List<Long> parameterIds,
            @Param("selectiveParameterIds") List<Long> selectiveParameterIds);

    @Query("SELECT DISTINCT h FROM House h " +
            "WHERE h.mainCategory.enabled = false OR h.subCategory.enabled = false " +
            "OR EXISTS (SELECT 1 FROM h.inputParameterValues ipv WHERE ipv.parameter.enabled = false) " +
            "OR EXISTS (SELECT 1 FROM h.selectiveParameterValues spv WHERE spv.enabled = false)" +
            "OR EXISTS (SELECT 1 FROM h.inputParameterValues ipv WHERE ipv.parameter.enabled = false)" +
            "OR EXISTS (SELECT 1 FROM h.selectiveParameterValues spv WHERE spv.parameter.enabled = false)")
    List<House> findDerelictHouses();
}
