package io.mingachevir.mingachevirrealestateserver.repository.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {

}
