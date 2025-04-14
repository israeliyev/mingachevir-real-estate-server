package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Table(name = "section")
@Getter
@Setter
public class Section extends BaseAuditEntity {

    @Column(name = "page")
    private String page;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "house_section",
            joinColumns = {@JoinColumn(name = "section_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id")}
    )
    private Set<House> houses = new HashSet<>();
}
