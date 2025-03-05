package com.chronelab.riscc.schedule;

import com.chronelab.riscc.entity.GroupQuestionnaireAnswerFinishedEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.QuestionQuestionnaireEntity;
import com.chronelab.riscc.entity.general.DeviceEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.Platform;
import com.chronelab.riscc.repo.AnswerRepo;
import com.chronelab.riscc.repo.GroupQuestionnaireAnswerFinishedRepo;
import com.chronelab.riscc.repo.GroupQuestionnaireRepo;
import com.chronelab.riscc.util.PushNotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Schedule {

    private static final Logger LOG = LogManager.getLogger();

    private final GroupQuestionnaireRepo groupQuestionnaireRepo;
    private final AnswerRepo answerRepo;
    private final GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo;

    @Autowired
    public Schedule(GroupQuestionnaireRepo groupQuestionnaireRepo, AnswerRepo answerRepo,
                    GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo) {
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
        this.answerRepo = answerRepo;
        this.groupQuestionnaireAnswerFinishedRepo = groupQuestionnaireAnswerFinishedRepo;
    }

    @Scheduled(fixedRate = 1000 * 60) // Every min
    @Transactional
    public void sendReminderNotification() throws FirebaseMessagingException, IOException {
        LOG.info("----- Reminder notification schedule is running. -----");
        List<GroupQuestionnaireEntity> groupQuestionnaireEntities = groupQuestionnaireRepo
                .findAllByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime.now(ZoneOffset.UTC),
                        LocalDateTime.now(ZoneOffset.UTC));
        if (groupQuestionnaireEntities != null && groupQuestionnaireEntities.size() > 0) {
            LOG.info("----- Found active Group Questionnaires. -----");
            for (GroupQuestionnaireEntity groupQuestionnaireEntity : groupQuestionnaireEntities) {

                List<QuestionEntity> questionEntities = groupQuestionnaireEntity.getQuestionnaire()
                        .getQuestionQuestionnaires().stream().map(QuestionQuestionnaireEntity::getQuestion)
                        .collect(Collectors.toList());
                List<UserEntity> users = groupQuestionnaireEntity.getGroup().getUsers();

                List<UserEntity> toExclude = new ArrayList<>();
                for (UserEntity userEntity : users) {
                    List<GroupQuestionnaireAnswerFinishedEntity> groupQuestionnaireAnswerFinishedEntities = groupQuestionnaireAnswerFinishedRepo
                            .findAllByUserAndGroupQuestionnaireAndFinishedDateTimeBetween(userEntity,
                                    groupQuestionnaireEntity, groupQuestionnaireEntity.getStartDateTime(),
                                    groupQuestionnaireEntity.getEndDateTime());
                    if (groupQuestionnaireAnswerFinishedEntities != null
                            && groupQuestionnaireAnswerFinishedEntities.size() > 0) {
                        toExclude.add(userEntity);
                    }
                }
                // users.removeAll(toExclude);

                // Prepare tokens for eligible users
                Map<String, Integer> androidTokenMap = new HashMap<>();
                Map<String, Integer> iosTokensSandboxMap = new HashMap<>();
                Map<String, Integer> iosTokensDistMap = new HashMap<>();

                Map<DeviceEntity, Integer> toUpdateBadgeCount = new HashMap<>();

                for (UserEntity userEntity : users) {
                    if (!toExclude.contains(userEntity)) {
                        if (questionEntities.size() != answerRepo.countAllByUserAndGroupQuestionnaire(userEntity,
                                groupQuestionnaireEntity)) {
                            if (userEntity.getDevice() != null && userEntity.getDevice().getToken() != null
                                    && !userEntity.getDevice().getToken().isEmpty()) {

                                int updatedBadgeCount = (userEntity.getDevice().getBadgeCount()) + 1;
                                toUpdateBadgeCount.put(userEntity.getDevice(), updatedBadgeCount);

                                if (userEntity.getDevice().getPlatform().equals(Platform.ANDROID)) {
                                    androidTokenMap.put(userEntity.getDevice().getToken(),
                                            userEntity.getDevice().getBadgeCount() + 1);
                                } else if (userEntity.getDevice().getPlatform().equals(Platform.IOS)) {
                                    if (userEntity.getDevice().getIosNotificationMode()
                                            .equals(IOSNotificationMode.SANDBOX)) {
                                        iosTokensSandboxMap.put(userEntity.getDevice().getToken(),
                                                userEntity.getDevice().getBadgeCount() + 1);
                                    } else {
                                        iosTokensDistMap.put(userEntity.getDevice().getToken(),
                                                userEntity.getDevice().getBadgeCount() + 1);
                                    }
                                }
                            }
                        }
                    }

                }

                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
                LocalDateTime temp = LocalDateTime.of(groupQuestionnaireEntity.getStartDateTime().getYear(),
                        groupQuestionnaireEntity.getStartDateTime().getMonth(),
                        groupQuestionnaireEntity.getStartDateTime().getDayOfMonth(),
                        groupQuestionnaireEntity.getStartDateTime().getHour(),
                        groupQuestionnaireEntity.getStartDateTime().getMinute(),
                        groupQuestionnaireEntity.getStartDateTime().getSecond());
                do {
                    if (temp.getYear() == now.getYear() && temp.getMonth() == now.getMonth()
                            && temp.getDayOfMonth() == now.getDayOfMonth() && temp.getHour() == now.getHour()
                            && temp.getMinute() == now.getMinute()) {

                        LOG.info("----- Sending reminder notification. -----");
                        // Send notification
                        if ((androidTokenMap.size() > 0 || iosTokensSandboxMap.size() > 0
                                || iosTokensDistMap.size() > 0)
                                && groupQuestionnaireEntity.getReminderMessage() != null) {
                            Map<String, String> messageData = new HashMap<>();
                            messageData.put("title", "Reminder");
                            messageData.put("description", groupQuestionnaireEntity.getReminderMessage());
                            Map<String, String> customFields = new HashMap<>();

                            PushNotificationUtil.sendPushNotification(Platform.ANDROID, androidTokenMap, messageData,
                                    customFields, null);
                            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensSandboxMap, messageData,
                                    customFields, IOSNotificationMode.SANDBOX);
                            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensDistMap, messageData,
                                    customFields, IOSNotificationMode.DIST);

                            for (Map.Entry<DeviceEntity, Integer> entry : toUpdateBadgeCount.entrySet()) {
                                entry.getKey().setBadgeCount(entry.getValue());
                            }
                        }
                        break;
                    }

                    if (groupQuestionnaireEntity.getReminderTimeInterval() != null && groupQuestionnaireEntity.getReminderTimeInterval() > 0) {
                        temp = temp.plusMinutes(groupQuestionnaireEntity.getReminderTimeInterval());
                    } else {
                        break;
                    }

                    //temp = temp.plusMinutes(groupQuestionnaireEntity.getReminderTimeInterval());
                } while (temp.isBefore(now));
            }
        }
        LOG.info("----- Reminder notification schedule stopped. -----");
    }
}
