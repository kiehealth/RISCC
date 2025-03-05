package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.request.GroupQuestionnaireAnswerFinishedReq;
import com.chronelab.riscc.entity.GroupQuestionnaireAnswerFinishedEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.GroupQuestionnaireAnswerFinishedRepo;
import com.chronelab.riscc.repo.GroupQuestionnaireRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupQuestionnaireAnswerFinishedService {

    private final GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo;
    private final UserRepo userRepo;
    private final GroupQuestionnaireRepo groupQuestionnaireRepo;

    @Autowired
    public GroupQuestionnaireAnswerFinishedService(GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo, UserRepo userRepo, GroupQuestionnaireRepo groupQuestionnaireRepo) {
        this.groupQuestionnaireAnswerFinishedRepo = groupQuestionnaireAnswerFinishedRepo;
        this.userRepo = userRepo;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
    }

    public void save(GroupQuestionnaireAnswerFinishedReq groupQuestionnaireAnswerFinishedReq) {
        log.info("----- Saving group questionnaire answer finished. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        Optional<GroupQuestionnaireEntity> optionalGroupQuestionnaireEntity = groupQuestionnaireRepo.findById(groupQuestionnaireAnswerFinishedReq.getGroupQuestionnaireId());
        optionalGroupQuestionnaireEntity.orElseThrow(() -> new CustomException("GRP003"));

        GroupQuestionnaireAnswerFinishedEntity groupQuestionnaireAnswerFinishedEntity = new GroupQuestionnaireAnswerFinishedEntity()
                .setGroupQuestionnaire(optionalGroupQuestionnaireEntity.get())
                .setUser(optionalUserEntity.get())
                .setFinishedDateTime(LocalDateTime.now(ZoneOffset.UTC))
                .setStartDateTime(optionalGroupQuestionnaireEntity.get().getStartDateTime())
                .setEndDateTime(optionalGroupQuestionnaireEntity.get().getEndDateTime());

        groupQuestionnaireAnswerFinishedRepo.save(groupQuestionnaireAnswerFinishedEntity);
    }
}
