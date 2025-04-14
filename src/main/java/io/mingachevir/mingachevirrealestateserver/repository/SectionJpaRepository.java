package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SectionJpaRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByEnabledIsTrueAndIsDeletedIsFalseAndPage(@Param("pageName") String pageName);
}
