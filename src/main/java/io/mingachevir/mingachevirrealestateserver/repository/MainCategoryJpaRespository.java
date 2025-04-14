package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface MainCategoryJpaRespository extends JpaRepository<MainCategory, Long> {
    @Query("SELECT DISTINCT m FROM MainCategory m " +
            "LEFT JOIN FETCH m.subCategories s " +
            "WHERE m.enabled = true AND m.isDeleted = false " +
            "AND (s IS NULL OR (s.enabled = true AND s.isDeleted = false))")
    List<MainCategory> findAllEnabledMainCategoriesWithEnabledSubCategories();

    List<MainCategory> findByEnabledTrueAndIsDeletedFalse();

    @Modifying
    @Query("UPDATE MainCategory mc SET mc.enabled = false, " +
            "mc.isDeleted = CASE WHEN EXISTS (SELECT 1 FROM House h WHERE h.mainCategory = mc) THEN false ELSE true END " +
            "WHERE mc.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids);
}
