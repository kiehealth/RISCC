package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.request.GroupQuestionnaireReq;
import com.chronelab.riscc.dto.response.GroupQuestionnaireRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.GroupQuestionnaireDtoUtil;
import com.chronelab.riscc.entity.GroupQuestionnaireAnswerFinishedEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.GroupQuestionnaireAnswerFinishedRepo;
import com.chronelab.riscc.repo.GroupQuestionnaireRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class GroupQuestionnaireService {

    private static final Logger LOG = LogManager.getLogger();

    private final GroupQuestionnaireRepo groupQuestionnaireRepo;
    private final UserRepo userRepo;
    private final GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo;
    private final DtoUtil<GroupQuestionnaireEntity, GroupQuestionnaireReq, GroupQuestionnaireRes> dtoUtil;

    @Autowired
    public GroupQuestionnaireService(GroupQuestionnaireRepo groupQuestionnaireRepo, UserRepo userRepo, GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo, GroupQuestionnaireDtoUtil groupQuestionnaireDtoUtil) {
        this.userRepo = userRepo;
        this.groupQuestionnaireAnswerFinishedRepo = groupQuestionnaireAnswerFinishedRepo;
        this.dtoUtil = groupQuestionnaireDtoUtil;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
    }

    @Transactional(readOnly = true)
    public List<GroupQuestionnaireRes> getActiveGroupQuestionnaire() {
        LOG.info("----- Getting Active Group Questionnaires. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        List<GroupQuestionnaireEntity> groupQuestionnaireEntities = groupQuestionnaireRepo.findAllByGroup_UsersInAndStartDateTimeBeforeAndEndDateTimeAfter(
                Arrays.asList(optionalUserEntity.get()), LocalDateTime.now(ZoneOffset.UTC), LocalDateTime.now(ZoneOffset.UTC)
        );

        List<GroupQuestionnaireEntity> toExclude = new ArrayList<>();

        for (GroupQuestionnaireEntity groupQuestionnaireEntity : groupQuestionnaireEntities) {
            List<GroupQuestionnaireAnswerFinishedEntity> groupQuestionnaireAnswerFinishedEntities =
                    groupQuestionnaireAnswerFinishedRepo.findAllByUserAndGroupQuestionnaireAndFinishedDateTimeBetween(
                            optionalUserEntity.get(), groupQuestionnaireEntity, groupQuestionnaireEntity.getStartDateTime(),
                            groupQuestionnaireEntity.getEndDateTime()
                    );
            if (groupQuestionnaireAnswerFinishedEntities != null) {
                toExclude.addAll(groupQuestionnaireAnswerFinishedEntities.stream().map(GroupQuestionnaireAnswerFinishedEntity::getGroupQuestionnaire).collect(Collectors.toList()));
            }
        }

        groupQuestionnaireEntities.removeAll(toExclude);

        return groupQuestionnaireEntities.stream().map(dtoUtil::prepRes).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GroupQuestionnaireRes> get() {
        LOG.info("----- Getting Group Questionnaires of Logged in user. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        List<GroupQuestionnaireEntity> groupQuestionnaireEntities = groupQuestionnaireRepo.findAllByGroup_UsersIn(Arrays.asList(optionalUserEntity.get()));
        return groupQuestionnaireEntities.stream().map(dtoUtil::prepRes).collect(Collectors.toList());
    }
}
