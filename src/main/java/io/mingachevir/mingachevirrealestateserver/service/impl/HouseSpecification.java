package io.mingachevir.mingachevirrealestateserver.service.impl;

import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.model.entity.InputParameterValue;
import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.InputParameterValueRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class HouseSpecification implements Specification<House> {

    private final GetFilterHousesRequest request;

    @Override
    public Predicate toPredicate(Root<House> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Base filters for enabled and non-deleted houses
        predicates.add(cb.equal(root.get("enabled"), true));
        predicates.add(cb.equal(root.get("isDeleted"), false));

        Join<House, SelectiveParameterValue> selectiveParamsJoin = root.join("selectiveParameterValues", JoinType.LEFT);
        Join<House, InputParameterValue> inputParamsJoin = root.join("inputParameterValues", JoinType.LEFT);
        Join<InputParameterValue, Parameters> paramJoin = inputParamsJoin.join("parameter", JoinType.LEFT);

        // Search key
        if (request.getSearchKey() != null && !request.getSearchKey().trim().isEmpty()) {
            String searchKeyLower = "%" + request.getSearchKey().toLowerCase() + "%";
            List<Predicate> searchPredicates = new ArrayList<>();
            searchPredicates.add(cb.like(cb.lower(root.get("name")), searchKeyLower));
            searchPredicates.add(cb.like(cb.lower(root.get("description")), searchKeyLower));

            Join<House, MainCategory> mainCategoryJoin = root.join("mainCategory", JoinType.LEFT);
            Join<House, SubCategory> subCategoryJoin = root.join("subCategory", JoinType.LEFT);
            searchPredicates.add(cb.like(cb.lower(mainCategoryJoin.get("name")), searchKeyLower));
            searchPredicates.add(cb.like(cb.lower(subCategoryJoin.get("name")), searchKeyLower));

            searchPredicates.add(cb.like(cb.lower(selectiveParamsJoin.get("name")), searchKeyLower));
            searchPredicates.add(cb.like(cb.lower(paramJoin.get("name")), searchKeyLower));

            predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
        }

        // Filter by main category ID
        if (request.getMainCategoryId() != null) {
            predicates.add(cb.equal(root.get("mainCategory").get("id"), request.getMainCategoryId()));
        }

        // Filter by sub category ID
        if (request.getSubCategoryId() != null) {
            predicates.add(cb.equal(root.get("subCategory").get("id"), request.getSubCategoryId()));
        }

        // Filter by selective parameter IDs
        if (request.getSelectiveParameterIds() != null && !request.getSelectiveParameterIds().isEmpty()) {
            predicates.add(selectiveParamsJoin.get("id").in(request.getSelectiveParameterIds()));
        }

        // Filter by input parameter ranges
        if (request.getInputParametersRanges() != null && !request.getInputParametersRanges().isEmpty()) {
            List<Predicate> rangePredicates = new ArrayList<>();
            for (InputParameterValueRequest range : request.getInputParametersRanges()) {
                Long parameterId = range.getParameterId();
                Integer minValue = range.getMin();
                Integer maxValue = range.getMax();

                // Validate range inputs
                if (parameterId == null || minValue == null || maxValue == null) {
                    continue; // Skip invalid ranges
                }
                if (minValue > maxValue) {
                    // Return empty list instead of error for invalid range
                    return cb.disjunction(); // No results
                }

                Predicate parameterIdPredicate = cb.equal(paramJoin.get("id"), parameterId);
                Predicate valueRangePredicate = cb.between(inputParamsJoin.get("value"), minValue, maxValue);
                rangePredicates.add(cb.and(parameterIdPredicate, valueRangePredicate));
            }
            if (!rangePredicates.isEmpty()) {
                predicates.add(cb.or(rangePredicates.toArray(new Predicate[0])));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
