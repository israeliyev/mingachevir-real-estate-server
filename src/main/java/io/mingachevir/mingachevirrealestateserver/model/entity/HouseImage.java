package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "house_image")
public class HouseImage extends BaseAuditEntity {
    private String path;
    private String fileName;
    private Long size;

    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private House house;

    @ManyToOne
    @JoinColumn(name = "house_request_id", referencedColumnName = "id")
    private HouseRequest houseRequest;
}
