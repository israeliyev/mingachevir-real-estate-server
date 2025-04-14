package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.SearchQuery;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "main_category")
public class MainCategory extends BaseAuditEntity {
    private String code;
    private String name;

    @OneToMany(mappedBy = "mainCategory", fetch = FetchType.LAZY)
    private List<SubCategory> subCategories;

    @OneToMany(mappedBy = "mainCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<SearchQuery> searchQueries;

    @OneToMany(mappedBy = "mainCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Parameters> parameters;
}


