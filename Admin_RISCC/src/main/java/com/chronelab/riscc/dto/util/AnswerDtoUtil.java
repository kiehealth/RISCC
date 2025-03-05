package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.AnswerReq;
import com.chronelab.riscc.dto.response.AnswerRes;
import com.chronelab.riscc.dto.response.QuestionRes;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AnswerDtoUtil implements DtoUtil<AnswerEntity, AnswerReq, AnswerRes> {

    private final QuestionDtoUtil questionDtoUtil;
    private final QuestionTypeDtoUtil questionTypeDtoUtil;
    private final QuestionnaireDtoUtil questionnaireDtoUtil;
    private final AnswerOptionDtoUtil answerOptionDtoUtil;
    private final UserDtoUtil userDtoUtil;

    @Autowired
    public AnswerDtoUtil(QuestionDtoUtil questionDtoUtil, QuestionTypeDtoUtil questionTypeDtoUtil, QuestionnaireDtoUtil questionnaireDtoUtil, AnswerOptionDtoUtil answerOptionDtoUtil, UserDtoUtil userDtoUtil) {
        this.questionDtoUtil = questionDtoUtil;
        this.questionTypeDtoUtil = questionTypeDtoUtil;
        this.questionnaireDtoUtil = questionnaireDtoUtil;
        this.answerOptionDtoUtil = answerOptionDtoUtil;
        this.userDtoUtil = userDtoUtil;
    }

    @Override
    public AnswerEntity reqToEntity(AnswerReq answerReq) {
        return new AnswerEntity()
                .setAnswer(answerReq.getAnswer());
    }

    @Override
    public AnswerRes entityToRes(AnswerEntity answerEntity) {
        return new AnswerRes()
                .setId(answerEntity.getId())
                .setAnswer(answerEntity.getAnswer())
                .setDateTime(answerEntity.getCreatedDate());
    }

    @Override
    public AnswerRes prepRes(AnswerEntity answerEntity) {
        AnswerRes answerRes = entityToRes(answerEntity);
        if (answerEntity.getAnswerOptions() != null && answerEntity.getAnswerOptions().size() > 0) {
            answerRes.setAnswerOptions(answerEntity.getAnswerOptions().stream().map(answerOptionDtoUtil::prepRes)
                    .collect(Collectors.toList()));

        }
        QuestionRes questionResDto = questionDtoUtil.entityToRes(answerEntity.getQuestion())
                .setQuestionType(questionTypeDtoUtil.entityToRes(answerEntity.getQuestion().getQuestionType()));
        answerRes.setQuestion(questionResDto);
        QuestionnaireRes questionnaireRes = questionnaireDtoUtil.entityToRes(answerEntity.getGroupQuestionnaire().getQuestionnaire());
        answerRes.setQuestionnaire(questionnaireRes);


        answerRes.setUser(userDtoUtil.entityToRes(answerEntity.getUser()));

        return answerRes;
    }

    @Override
    public void setUpdatedValue(AnswerReq answerReq, AnswerEntity answerEntity) {

    }
}
