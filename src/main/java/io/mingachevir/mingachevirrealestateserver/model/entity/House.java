package io.mingachevir.mingachevirrealestateserver.model.entity;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.BasketAddition;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.HouseView;
import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseAuditEntity;
import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "house")
public class House extends BaseAuditEntity {
    private String name;
    private Double price;
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
    private String location;
    private String description;
    private String coverImage;
    private String houseVideo;

    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<HouseImage> houseImages;

    @ManyToMany
    @JoinTable(name = "house_selective_parameter_value",
            
            joinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "selective_parameter_id", referencedColumnName = "id")})
    private Set<SelectiveParameterValue> selectiveParameterValues = new HashSet<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<InputParameterValue> inputParameterValues;

    @ManyToMany
    @JoinTable(
            name = "house_section",
            
            joinColumns = {@JoinColumn(name = "house_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "section_id", referencedColumnName = "id")}
    )
    private Set<Section> sections = new HashSet<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<BasketAddition> basketAdditions;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<HouseView> houseViews;
}
