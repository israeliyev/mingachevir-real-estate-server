package io.mingachevir.mingachevirrealestateserver.repository.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.InputParameterRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputParameterRangeRepository extends JpaRepository<InputParameterRange, Long> {
    @Query("SELECT p.id, p.name, ipr.minValue, ipr.maxValue, COUNT(ipr) as rangeCount " +
           "FROM InputParameterRange ipr JOIN ipr.parameter p " +
           "GROUP BY p.id, p.name, ipr.minValue, ipr.maxValue " +
           "ORDER BY rangeCount DESC")
    List<Object[]> findMostSearchedInputParameters();
}
