package io.mingachevir.mingachevirrealestateserver.model.entity.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity

public class InputParameterRange extends BaseAuditEntity {
    // Getters and Setters
    @ManyToOne
    @JoinColumn(name = "parameter_id", referencedColumnName = "id")
    private Parameters parameter;

    private Integer minValue;
    private Integer maxValue;

    @ManyToOne
    @JoinColumn(name = "search_query_id", referencedColumnName = "id")
    private SearchQuery searchQuery;

}
