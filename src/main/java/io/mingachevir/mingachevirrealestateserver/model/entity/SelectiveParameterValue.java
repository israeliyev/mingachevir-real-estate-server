package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "selective_parameter_value")
public class SelectiveParameterValue extends BaseAuditEntity {
    private String code;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parameter_id", referencedColumnName = "id")
    private Parameters parameter;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "selectiveParameterValues")
    private Set<House> houses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "selectiveParameterValues")
    private Set<HouseRequest> houseRquests = new HashSet<>();
}
