package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.GroupQuestionnaireReq;
import com.chronelab.riscc.dto.request.GroupReq;
import com.chronelab.riscc.dto.response.GroupRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.GroupDtoUtil;
import com.chronelab.riscc.dto.util.GroupQuestionnaireDtoUtil;
import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.GroupQuestionnaireRepo;
import com.chronelab.riscc.repo.GroupRepo;
import com.chronelab.riscc.repo.QuestionnaireRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('GROUP')")
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupService {

    private static final Logger LOG = LogManager.getLogger();

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final DtoUtil<GroupEntity, GroupReq, GroupRes> dtoUtil;
    private final PaginationDtoUtil<GroupEntity, GroupReq, GroupRes> paginationDtoUtil;
    private final QuestionnaireRepo questionnaireRepo;
    private final GroupQuestionnaireDtoUtil groupQuestionnaireDtoUtil;
    private final GroupQuestionnaireRepo groupQuestionnaireRepo;

    @Autowired
    public GroupService(GroupRepo groupRepo, UserRepo userRepo, GroupDtoUtil groupDtoUtil, PaginationDtoUtil<GroupEntity, GroupReq, GroupRes> paginationDtoUtil, QuestionnaireRepo questionnaireRepo, GroupQuestionnaireDtoUtil groupQuestionnaireDtoUtil, GroupQuestionnaireRepo groupQuestionnaireRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.dtoUtil = groupDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
        this.questionnaireRepo = questionnaireRepo;
        this.groupQuestionnaireDtoUtil = groupQuestionnaireDtoUtil;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
    }

    @PreAuthorize("hasAuthority('GROUP_C')")
    public GroupRes save(GroupReq groupReq) {
        LOG.info("----- Saving Group. -----");

        groupRepo.findByTitle(groupReq.getTitle()).ifPresent((groupEntity) -> {
            throw new CustomException("GRP001");
        });

        GroupEntity group = dtoUtil.reqToEntity(groupReq);

        //Group Member
        if (groupReq.getUserIds() != null && groupReq.getUserIds().size() > 0) {
            group.setUsers(
                    groupReq.getUserIds().stream().map(id -> {
                        Optional<UserEntity> optionalUserEntity = userRepo.findById(id);
                        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                        return optionalUserEntity.get();
                    }).collect(Collectors.toList())
            );
        }

        //Group Questionnaire
        if (groupReq.getGroupQuestionnaires() != null) {
            List<GroupQuestionnaireEntity> groupQuestionnaires = new ArrayList<>();
            for (GroupQuestionnaireReq groupQuestionnaireReq : groupReq.getGroupQuestionnaires()) {
                if (groupQuestionnaireReq.getQuestionnaireId() != null) {
                    Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(groupQuestionnaireReq.getQuestionnaireId());
                    optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));

                    GroupQuestionnaireEntity groupQuestionnaire = groupQuestionnaireDtoUtil.reqToEntity(groupQuestionnaireReq)
                            .setQuestionnaire(optionalQuestionnaireEntity.get())
                            .setGroup(group);

                    groupQuestionnaires.add(groupQuestionnaire);
                }
            }

            group.setGroupQuestionnaires(groupQuestionnaires);
        }
        return dtoUtil.prepRes(groupRepo.save(group));
    }
    @PreAuthorize("hasAuthority('USER_RA')")
    public PaginationResDto<GroupRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Group. -----");

        List<String> fields = Arrays.asList("title", "description","users");
        String sortBy = "title";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        if (paginationReqDto.getSortBy() != null) {
            if (paginationReqDto.getSortBy().equals("title")) {
                sortBy = "title";
            } else if (paginationReqDto.getSortBy().equals("users")) {
                sortBy = "users";
            } else {
                sortBy = paginationReqDto.getSortBy();
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, groupRepo, dtoUtil);
    }

    public List<GroupRes> getByUser() {
        LOG.info("----- Getting group by logged in user. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        List<GroupEntity> groupEntities = groupRepo.findAllByUsersIn(Arrays.asList(optionalUserEntity.get()));

        return groupEntities
                .stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('GROUP_U')")
    public GroupRes update(GroupReq groupReq) {
        LOG.info("----- Updating Group. -----");

        Optional<GroupEntity> optionalGroupEntity = groupRepo.findById(groupReq.getId());
        optionalGroupEntity.orElseThrow(() -> new CustomException("GRP002"));
        GroupEntity groupEntity = optionalGroupEntity.get();

        if (groupReq.getTitle() != null && !groupReq.getTitle().equalsIgnoreCase(groupEntity.getTitle())
                && groupRepo.findByTitle(groupReq.getTitle()).isPresent()) {
            throw new CustomException("GRP001");
        }


        //Group Member
        if (groupReq.getUserIds() != null) {

            //Add Users sent from frontend
            List<UserEntity> newUsers = new ArrayList<>();
            for (Long userId : groupReq.getUserIds()) {
                Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                UserEntity userEntity = optionalUserEntity.get();

                newUsers.add(userEntity);
                if (!groupEntity.getUsers().contains(userEntity)) {
                    groupEntity.getUsers().add(userEntity);//Add new users
                }
            }

            //Remove Users
            List<UserEntity> usersToRemove = new ArrayList<>();
            for (UserEntity userEntity : groupEntity.getUsers()) {
                if (!newUsers.contains(userEntity)) {
                    usersToRemove.add(userEntity);
                }
            }
            groupEntity.getUsers().removeAll(usersToRemove);
        }

        if (groupReq.getGroupQuestionnaires() != null) {

            //Add Questionnaire sent from frontend
            List<GroupQuestionnaireEntity> newGroupQuestionnaires = new ArrayList<>();
            List<GroupQuestionnaireEntity> existingGroupQuestionnaires = new ArrayList<>();

            for (GroupQuestionnaireReq groupQuestionnaireReq : groupReq.getGroupQuestionnaires()) {
                if (groupQuestionnaireReq.getId() != null) {
                    Optional<GroupQuestionnaireEntity> optionalGroupQuestionnaireEntity = groupQuestionnaireRepo.findById(groupQuestionnaireReq.getId());
                    optionalGroupQuestionnaireEntity.orElseThrow(() -> new CustomException("GRP003"));

                    existingGroupQuestionnaires.add(optionalGroupQuestionnaireEntity.get());

                    groupQuestionnaireDtoUtil.setUpdatedValue(groupQuestionnaireReq, optionalGroupQuestionnaireEntity.get());
                    if (groupQuestionnaireReq.getQuestionnaireId() != null &&
                            !groupQuestionnaireReq.getQuestionnaireId().equals(optionalGroupQuestionnaireEntity.get().getQuestionnaire().getId())) {
                        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(groupQuestionnaireReq.getQuestionnaireId());
                        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                        optionalGroupQuestionnaireEntity.get().setQuestionnaire(optionalQuestionnaireEntity.get());
                    }
                } else {
                    GroupQuestionnaireEntity groupQuestionnaire = groupQuestionnaireDtoUtil.reqToEntity(groupQuestionnaireReq);

                    Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(groupQuestionnaireReq.getQuestionnaireId());
                    optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));

                    groupQuestionnaire.setQuestionnaire(optionalQuestionnaireEntity.get());
                    groupQuestionnaire.setGroup(optionalGroupEntity.get());

                    newGroupQuestionnaires.add(groupQuestionnaire);
                }
            }
            groupEntity.getGroupQuestionnaires().addAll(newGroupQuestionnaires);

            //Remove Questionnaire
            List<GroupQuestionnaireEntity> groupQuestionnaireToRemove = new ArrayList<>();
            for (GroupQuestionnaireEntity groupQuestionnaireEntity : groupEntity.getGroupQuestionnaires()) {
                if (!existingGroupQuestionnaires.contains(groupQuestionnaireEntity) && groupQuestionnaireEntity.getId() != null) {
                    groupQuestionnaireToRemove.add(groupQuestionnaireEntity);
                }
            }
            groupEntity.getGroupQuestionnaires().removeAll(groupQuestionnaireToRemove);
            groupQuestionnaireRepo.deleteAll(groupQuestionnaireToRemove);
        }

        dtoUtil.setUpdatedValue(groupReq, groupEntity);
        return dtoUtil.prepRes(groupEntity);
    }

    @PreAuthorize("hasAuthority('GROUP_D')")
    public void delete(Long id) {
        LOG.info("----- Deleting Group. -----");

        Optional<GroupEntity> optionalGroupEntity = groupRepo.findById(id);
        optionalGroupEntity.orElseThrow(() -> new CustomException("GRP002"));

        if (optionalGroupEntity.get().getGroupQuestionnaires() != null) {
            groupQuestionnaireRepo.deleteAllByGroup(optionalGroupEntity.get());
        }

        groupRepo.delete(optionalGroupEntity.get());
    }
}
