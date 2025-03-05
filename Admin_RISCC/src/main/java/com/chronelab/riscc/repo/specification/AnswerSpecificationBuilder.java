package com.chronelab.riscc.repo.specification;

import com.chronelab.riscc.entity.AnswerEntity;
import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.QuestionQuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerSpecificationBuilder {
    private final List<SearchCriteria> params;

    public AnswerSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public AnswerSpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<AnswerEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<AnswerEntity>> specs = params.stream()
                .map(AnswerSpecification::new)
                .collect(Collectors.toList());

        Specification<AnswerEntity> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            if (params.get(i).getKey().equals("questionTitle")
                    || params.get(i).getKey().equals("questionBody")) {
                result = Specification.where(result).or(specs.get(i));
            } else {
                result = Specification.where(result).and(specs.get(i));
            }
        }
        return result;
    }
}

class AnswerSpecification implements Specification<AnswerEntity> {

    private SearchCriteria criteria;

    public AnswerSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<AnswerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equalsIgnoreCase("userIds")) {
            Join<AnswerEntity, UserEntity> answerUserJoin = root.join("user", JoinType.INNER);
            return answerUserJoin.in(criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("questionIds")) {
            Join<AnswerEntity, QuestionEntity> answerQuestionJoin = root.join("question", JoinType.INNER);
            return answerQuestionJoin.in(criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("questionnaireIds")) {
            Join<AnswerEntity, QuestionEntity> answerQuestionJoin = root.join("question", JoinType.INNER);
            Join<QuestionEntity, QuestionQuestionnaireEntity> questionQuestionnaireJoin = answerQuestionJoin.join("questionQuestionnaires", JoinType.INNER);
            return questionQuestionnaireJoin.get("questionnaire").in(criteria.getValue());
        } else if (criteria.getKey().equalsIgnoreCase("questionTitle")) {
            Join<AnswerEntity, QuestionEntity> answerQuestionJoin = root.join("question", JoinType.INNER);
            return builder.like(answerQuestionJoin.get("title"), "%" + criteria.getValue() + "%");
        } else if (criteria.getKey().equalsIgnoreCase("questionBody")) {
            Join<AnswerEntity, QuestionEntity> answerQuestionJoin = root.join("question", JoinType.INNER);
            return builder.like(answerQuestionJoin.get("body"), "%" + criteria.getValue() + "%");
        } else if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

    }
}
