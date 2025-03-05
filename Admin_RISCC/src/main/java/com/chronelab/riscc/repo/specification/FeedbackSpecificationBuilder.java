package com.chronelab.riscc.repo.specification;

import com.chronelab.riscc.entity.FeedbackEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackSpecificationBuilder {

    private final List<SearchCriteria> params;

    public FeedbackSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public FeedbackSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<FeedbackEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<FeedbackEntity>> specs = params.stream()
                .map(FeedbackSpecification::new)
                .collect(Collectors.toList());

        Specification<FeedbackEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class FeedbackSpecification implements Specification<FeedbackEntity> {

    private SearchCriteria criteria;

    public FeedbackSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<FeedbackEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

    }
}