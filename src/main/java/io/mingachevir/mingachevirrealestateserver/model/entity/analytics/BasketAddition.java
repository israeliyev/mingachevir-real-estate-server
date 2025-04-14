package io.mingachevir.mingachevirrealestateserver.model.entity.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity

public class BasketAddition extends BaseAuditEntity {
    // Getters and Setters
    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private House house;

    private Date additionDateTime;
    private String visitorIp; // Optional
    private String sessionId; // Optional

}
