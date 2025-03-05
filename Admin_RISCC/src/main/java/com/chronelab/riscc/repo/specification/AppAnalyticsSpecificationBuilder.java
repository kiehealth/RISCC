package com.chronelab.riscc.repo.specification;

import com.chronelab.riscc.entity.AppAnalyticsEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppAnalyticsSpecificationBuilder {
    private final List<SearchCriteria> params;

    public AppAnalyticsSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public AppAnalyticsSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<AppAnalyticsEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<AppAnalyticsEntity>> specs = params.stream()
                .map(AppAnalyticsSpecification::new)
                .collect(Collectors.toList());

        Specification<AppAnalyticsEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class AppAnalyticsSpecification implements Specification<AppAnalyticsEntity> {

    private SearchCriteria criteria;

    public AppAnalyticsSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<AppAnalyticsEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equalsIgnoreCase("userIds")) {
            Join<AppAnalyticsEntity, UserEntity> answerUserJoin = root.join("user", JoinType.INNER);
            return answerUserJoin.in(criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("appAnalyticsKey")) {
            Join<AppAnalyticsEntity, AppAnalyticsKeyEntity> appAnalyticsKeyJoin = root.join("appAnalyticsKey", JoinType.INNER);
            return appAnalyticsKeyJoin.in(criteria.getValue());
        } else if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

    }
}
