package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ParameterJpaRepository extends JpaRepository<Parameters, Long> {
    List<Parameters> findAllByEnabledIsTrueAndIsDeletedFalseOrSubCategoryIdIsNull(Pageable pageable);

    List<Parameters> findAllByEnabledIsTrueAndIsDeletedFalseAndSubCategoryIdIsNullOrSubCategoryId(@Param("subCategoryId") Long subCategoryId);

    List<Parameters> findByEnabledTrueAndIsDeletedFalse();

    @Modifying
    @Query("UPDATE Parameters p SET p.enabled = false, " +
            "p.isDeleted = CASE WHEN EXISTS (" +
            "SELECT 1 FROM House h " +
            "JOIN h.inputParameterValues ipv " +
            "WHERE ipv.parameter = p) OR EXISTS (" +
            "SELECT 1 FROM House h " +
            "JOIN h.selectiveParameterValues spv " +
            "WHERE spv.parameter = p) THEN false ELSE true END " +
            "WHERE p.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids);
}
