package io.mingachevir.mingachevirrealestateserver.model.entity.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity

public class SiteVisit extends BaseAuditEntity {
    // Getters and Setters
    private Date visitDateTime;
    private String visitorIp; // Optional: Track IP for unique visitors
    private String sessionId; // Optional: Track session for unique visits

}
