package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubCategoryJpaRepository extends JpaRepository<SubCategory, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE SubCategory m SET m.enabled = false WHERE m.id IN :ids")
    void disableSubCategories(@Param("ids") List<Long> ids);

    List<SubCategory> findByEnabledTrueAndIsDeletedFalse();

    @Modifying
    @Query("UPDATE SubCategory sc SET sc.enabled = false, " +
            "sc.isDeleted = CASE WHEN EXISTS (SELECT 1 FROM House h WHERE h.subCategory = sc) THEN false ELSE true END " +
            "WHERE sc.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids);

    List<SubCategory> findAllByMainCategoryIsNullAndEnabledTrueAndIsDeletedFalse();
}
