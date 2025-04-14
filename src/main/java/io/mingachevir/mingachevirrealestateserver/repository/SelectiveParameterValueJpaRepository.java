package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SelectiveParameterValueJpaRepository extends JpaRepository<SelectiveParameterValue, Long> {

    List<SelectiveParameterValue> findByEnabledTrueAndIsDeletedFalse();

    @Modifying
    @Query("UPDATE SelectiveParameterValue spv SET spv.enabled = false, " +
            "spv.isDeleted = CASE WHEN EXISTS (SELECT 1 FROM House h WHERE spv MEMBER OF h.selectiveParameterValues) THEN false ELSE true END " +
            "WHERE spv.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids);
}
