package com.chronelab.riscc.repo.specification;

import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppAnalyticsKeySpecificationBuilder {

    private final List<SearchCriteria> params;

    public AppAnalyticsKeySpecificationBuilder() {
        params = new ArrayList<>();
    }

    public AppAnalyticsKeySpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<AppAnalyticsKeyEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<AppAnalyticsKeyEntity>> specs = params.stream()
                .map(AppAnalyticsKeySpecification::new)
                .collect(Collectors.toList());

        Specification<AppAnalyticsKeyEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class AppAnalyticsKeySpecification implements Specification<AppAnalyticsKeyEntity> {

    private SearchCriteria criteria;

    public AppAnalyticsKeySpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<AppAnalyticsKeyEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equalsIgnoreCase("title1")
                || criteria.getKey().equalsIgnoreCase("title2")) {
            return builder.like(root.get("keyTitle"), "%" + criteria.getValue());
        } else if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

    }
}