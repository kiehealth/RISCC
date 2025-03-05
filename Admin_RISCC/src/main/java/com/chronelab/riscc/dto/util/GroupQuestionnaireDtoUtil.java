package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.GroupQuestionnaireReq;
import com.chronelab.riscc.dto.response.GroupQuestionnaireRes;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupQuestionnaireDtoUtil implements DtoUtil<GroupQuestionnaireEntity, GroupQuestionnaireReq, GroupQuestionnaireRes> {

    private final QuestionnaireDtoUtil questionnaireDtoUtil;

    @Autowired
    public GroupQuestionnaireDtoUtil(QuestionnaireDtoUtil questionnaireDtoUtil) {
        this.questionnaireDtoUtil = questionnaireDtoUtil;
    }

    @Override
    public GroupQuestionnaireEntity reqToEntity(GroupQuestionnaireReq groupQuestionnaireReq) {
        return new GroupQuestionnaireEntity()
                .setStartDateTime(groupQuestionnaireReq.getStartDateTime())
                .setEndDateTime(groupQuestionnaireReq.getEndDateTime())
                .setReminderMessage(groupQuestionnaireReq.getReminderMessage())
                .setReminderTimeInterval(groupQuestionnaireReq.getReminderTimeInterval());
    }

    @Override
    public GroupQuestionnaireRes entityToRes(GroupQuestionnaireEntity groupQuestionnaireEntity) {
        return new GroupQuestionnaireRes()
                .setId(groupQuestionnaireEntity.getId())
                .setStartDateTime(groupQuestionnaireEntity.getStartDateTime())
                .setEndDateTime(groupQuestionnaireEntity.getEndDateTime())
                .setReminderMessage(groupQuestionnaireEntity.getReminderMessage())
                .setReminderTimeInterval(groupQuestionnaireEntity.getReminderTimeInterval());
    }

    @Override
    public GroupQuestionnaireRes prepRes(GroupQuestionnaireEntity groupQuestionnaireEntity) {
        return entityToRes(groupQuestionnaireEntity)
                .setQuestionnaire(questionnaireDtoUtil.entityToRes(groupQuestionnaireEntity.getQuestionnaire()));
    }

    @Override
    public void setUpdatedValue(GroupQuestionnaireReq groupQuestionnaireReq, GroupQuestionnaireEntity groupQuestionnaireEntity) {
        if (groupQuestionnaireReq != null && groupQuestionnaireEntity != null) {
            if (groupQuestionnaireReq.getStartDateTime() != null && !groupQuestionnaireReq.getStartDateTime().equals(groupQuestionnaireEntity.getStartDateTime())) {
                groupQuestionnaireEntity.setStartDateTime(groupQuestionnaireReq.getStartDateTime());
            }
            if (groupQuestionnaireReq.getEndDateTime() != null && !groupQuestionnaireReq.getEndDateTime().equals(groupQuestionnaireEntity.getEndDateTime())) {
                groupQuestionnaireEntity.setEndDateTime(groupQuestionnaireReq.getEndDateTime());
            }

            if (groupQuestionnaireReq.getReminderMessage() != null &&
                    (groupQuestionnaireEntity.getReminderMessage() == null || !groupQuestionnaireReq.getReminderMessage().equals(groupQuestionnaireEntity.getReminderMessage()))) {
                groupQuestionnaireEntity.setReminderMessage(groupQuestionnaireReq.getReminderMessage());
            }

            if (groupQuestionnaireReq.getReminderTimeInterval() != null &&
                    (groupQuestionnaireEntity.getReminderTimeInterval() == null || !groupQuestionnaireReq.getReminderTimeInterval().equals(groupQuestionnaireEntity.getReminderTimeInterval()))) {
                groupQuestionnaireEntity.setReminderTimeInterval(groupQuestionnaireReq.getReminderTimeInterval());
            }
        }
    }
}
