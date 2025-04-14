package io.mingachevir.mingachevirrealestateserver.model.entity.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity

public class SearchQuery extends BaseAuditEntity {
    // Getters and Setters
    private String searchKey; // The searched keyword

    @ManyToOne
    @JoinColumn(name = "main_category_id", referencedColumnName = "id")
    private MainCategory mainCategory;

    @ManyToOne
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id")
    private SubCategory subCategory;

    @ManyToMany
    @JoinTable(name = "search_query_selective_parameters",
            
            joinColumns = @JoinColumn(name = "search_query_id"),
            inverseJoinColumns = @JoinColumn(name = "selective_parameter_value_id"))
    private Set<SelectiveParameterValue> selectiveParameterValues;

    @OneToMany(mappedBy = "searchQuery", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<InputParameterRange> inputParameterRanges;

    private Date searchDateTime;
    private String visitorIp;
    private String sessionId;

    public void addInputParameterRange(InputParameterRange range) {
        inputParameterRanges.add(range);
        range.setSearchQuery(this);
    }

    public void setInputParameterRanges(List<InputParameterRange> inputParameterRanges) {
        this.inputParameterRanges = new ArrayList<>();
        inputParameterRanges.forEach(this::addInputParameterRange);
    }
}
