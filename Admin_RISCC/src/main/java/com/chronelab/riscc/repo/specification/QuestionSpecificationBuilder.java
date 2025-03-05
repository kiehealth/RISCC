package com.chronelab.riscc.repo.specification;

import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.QuestionQuestionnaireEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionSpecificationBuilder {
    private final List<SearchCriteria> params;

    public QuestionSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public QuestionSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<QuestionEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<QuestionEntity>> specs = params.stream()
                .map(QuestionSpecification::new)
                .collect(Collectors.toList());

        Specification<QuestionEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}

class QuestionSpecification implements Specification<QuestionEntity> {

    private SearchCriteria criteria;

    public QuestionSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<QuestionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equalsIgnoreCase("questionnaireIds")) {
            Join<QuestionEntity, QuestionQuestionnaireEntity> questionQuestionnaireJoin = root.join("questionQuestionnaires", JoinType.INNER);
            return questionQuestionnaireJoin.get("questionnaire").in(criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("questionTitle")) {
            return builder.equal(root.get("title"), criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("questionBody")) {
            return builder.equal(root.get("body"), criteria.getValue());
        } else if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

    }
}
