package io.mingachevir.mingachevirrealestateserver.repository.analytics;

import io.mingachevir.mingachevirrealestateserver.model.dto.HouseViewSummaryDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.BasketAddition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketAdditionRepository extends JpaRepository<BasketAddition, Long> {

    @Query("SELECT new io.mingachevir.mingachevirrealestateserver.model.dto.HouseViewSummaryDto(" +
            "h.id, h.coverImage, h.price, h.priceType, h.name, h.location, COUNT(hv)) " +
            "FROM BasketAddition hv " +
            "JOIN hv.house h " +
            "WHERE h.isDeleted = false AND h.enabled = true " +
            "GROUP BY h.id, h.coverImage, h.price, h.priceType, h.name, h.location " +
            "ORDER BY COUNT(hv) DESC")
    Page<HouseViewSummaryDto> findAllHousesByLikeCount(Pageable pageable);
}
