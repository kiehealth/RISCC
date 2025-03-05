package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupQuestionnaireRepo extends JpaRepository<GroupQuestionnaireEntity, Long> {
    void deleteAllByGroup(GroupEntity groupEntity);

    List<GroupQuestionnaireEntity> findAllByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    List<GroupQuestionnaireEntity> findAllByGroup_UsersInAndStartDateTimeBeforeAndEndDateTimeAfter(List<UserEntity> userEntities, LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    List<GroupQuestionnaireEntity> findAllByGroup_UsersIn(List<UserEntity> userEntities);
}
