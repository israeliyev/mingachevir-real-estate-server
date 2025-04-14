package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "input_parameter_value")
@ToString
public class InputParameterValue extends BaseAuditEntity {
    private String code;
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "parameter_id", referencedColumnName = "id")
    private Parameters parameter;

    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_request_id", referencedColumnName = "id")
    private HouseRequest houseRequest;
}
