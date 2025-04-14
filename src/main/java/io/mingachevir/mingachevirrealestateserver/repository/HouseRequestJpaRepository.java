package io.mingachevir.mingachevirrealestateserver.repository;

import io.mingachevir.mingachevirrealestateserver.model.entity.HouseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRequestJpaRepository extends JpaRepository<HouseRequest, Long> {
    List<HouseRequest> findAllByEnabledIsTrueAndIsDeletedIsFalse();
}
