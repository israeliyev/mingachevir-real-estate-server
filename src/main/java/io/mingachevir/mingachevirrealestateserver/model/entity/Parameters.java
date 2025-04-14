package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.InputParameterRange;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import io.mingachevir.mingachevirrealestateserver.util.ParameterTypeEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "parameters")
public class Parameters extends BaseAuditEntity {
    private String code;
    private String name;
    @Enumerated(EnumType.STRING)
    private ParameterTypeEnum type;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id")
    private SubCategory subCategory;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "main_category_id", referencedColumnName = "id")
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "parameter", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<SelectiveParameterValue> selectiveParameterValues;

    @OneToMany(mappedBy = "parameter", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<InputParameterValue> inputParameterValues;

    @OneToMany(mappedBy = "parameter", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<InputParameterRange> inputParameterRanges;
}
