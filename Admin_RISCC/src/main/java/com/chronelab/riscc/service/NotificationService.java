package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.NotificationDto;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.NotificationReq;
import com.chronelab.riscc.dto.response.NotificationRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.NotificationDtoUtil;
import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.NotificationEntity;
import com.chronelab.riscc.entity.general.DeviceEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.NotificationType;
import com.chronelab.riscc.enums.Platform;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.GroupRepo;
import com.chronelab.riscc.repo.NotificationRepo;
import com.chronelab.riscc.repo.general.DeviceRepo;
import com.chronelab.riscc.repo.general.RoleRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.util.PushNotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@PreAuthorize("hasAuthority('NOTIFICATION')")
public class NotificationService {

    private static final Logger LOG = LogManager.getLogger();

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;
    private final DeviceRepo deviceRepo;
    private final RoleRepo roleRepo;
    private final GroupRepo groupRepo;
    private final DtoUtil<NotificationEntity, NotificationReq, NotificationRes> dtoUtil;
    private final PaginationDtoUtil<NotificationEntity, NotificationReq, NotificationRes> paginationDtoUtil;

    @Autowired
    public NotificationService(NotificationRepo notificationRepo, UserRepo userRepo, DeviceRepo deviceRepo,
                               RoleRepo roleRepo, GroupRepo groupRepo, NotificationDtoUtil notificationDtoUtil, PaginationDtoUtil<NotificationEntity, NotificationReq, NotificationRes> paginationDtoUtil) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
        this.deviceRepo = deviceRepo;
        this.roleRepo = roleRepo;
        this.groupRepo = groupRepo;
        this.dtoUtil = notificationDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('NOTIFICATION_C')")
    public NotificationRes save(NotificationReq notificationReqDto) throws FirebaseMessagingException, IOException {
        LOG.info("----- Saving Notification. -----");
        NotificationEntity notification = dtoUtil.reqToEntity(notificationReqDto);

        Map<String, Integer> androidTokenMap = new HashMap<>();
        Map<String, Integer> iosTokensSandboxMap = new HashMap<>();
        Map<String, Integer> iosTokensDistMap = new HashMap<>();

        if (notificationReqDto.getNotificationType().equals(NotificationType.BROADCAST)) {
            List<DeviceEntity> androidDeviceEntities = deviceRepo.findAllByPlatformAndTokenNotNullAndLoggedOutFalse(Platform.ANDROID);
            for (DeviceEntity deviceEntity : androidDeviceEntities) {
                deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                androidTokenMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
            }

            List<DeviceEntity> iosDeviceEntities = deviceRepo.findAllByPlatformAndTokenNotNullAndLoggedOutFalse(Platform.IOS);
            for (DeviceEntity deviceEntity : iosDeviceEntities) {
                deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                if (deviceEntity.getIosNotificationMode() != null && deviceEntity.getIosNotificationMode().equals(IOSNotificationMode.SANDBOX)) {
                    iosTokensSandboxMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                } else {
                    iosTokensDistMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                }
            }

        } else if (notificationReqDto.getNotificationType().equals(NotificationType.ROLE)) {
            if (notificationReqDto.getRoleIds() == null || notificationReqDto.getRoleIds().size() == 0) {
                throw new CustomException("ROL004");
            }
            List<RoleEntity> roleEntities = new ArrayList<>();
            for (Long roleId : notificationReqDto.getRoleIds()) {
                Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleId);
                optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
                roleEntities.add(optionalRoleEntity.get());

                List<DeviceEntity> androidDeviceEntities = deviceRepo.findAllByPlatformAndUser_RoleAndTokenNotNullAndLoggedOutFalse(Platform.ANDROID, optionalRoleEntity.get());
                for (DeviceEntity deviceEntity : androidDeviceEntities) {
                    deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                    androidTokenMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                }

                List<DeviceEntity> iosDeviceEntities = deviceRepo.findAllByPlatformAndUser_RoleAndTokenNotNullAndLoggedOutFalse(Platform.IOS, optionalRoleEntity.get());
                for (DeviceEntity deviceEntity : iosDeviceEntities) {
                    deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                    if (deviceEntity.getIosNotificationMode() != null && deviceEntity.getIosNotificationMode().equals(IOSNotificationMode.SANDBOX)) {
                        iosTokensSandboxMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                    } else {
                        iosTokensDistMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                    }
                }
            }
            notification.setRoles(roleEntities);

        } else if (notificationReqDto.getNotificationType().equals(NotificationType.GROUP)) {
            if (notificationReqDto.getGroupIds() == null || notificationReqDto.getGroupIds().size() == 0) {
                throw new CustomException("GRP005");
            }

            List<GroupEntity> groupEntities = new ArrayList<>();
            for (Long groupId : notificationReqDto.getGroupIds()) {
                Optional<GroupEntity> optionalGroupEntity = groupRepo.findById(groupId);
                optionalGroupEntity.orElseThrow(() -> new CustomException("GRP002"));
                groupEntities.add(optionalGroupEntity.get());
            }
            notification.setGroups(groupEntities);


            List<DeviceEntity> androidDeviceEntities = deviceRepo.findAllByPlatformAndUser_GroupsInAndTokenNotNullAndTokenNotAndLoggedOutFalse(Platform.ANDROID, groupEntities, "");
            for (DeviceEntity deviceEntity : androidDeviceEntities) {
                deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                androidTokenMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
            }

            List<DeviceEntity> iosDeviceEntities = deviceRepo.findAllByPlatformAndUser_GroupsInAndTokenNotNullAndTokenNotAndLoggedOutFalse(Platform.IOS, groupEntities, "");
            for (DeviceEntity deviceEntity : iosDeviceEntities) {
                deviceEntity.setBadgeCount(deviceEntity.getBadgeCount() + 1);
                if (deviceEntity.getIosNotificationMode() != null && deviceEntity.getIosNotificationMode().equals(IOSNotificationMode.SANDBOX)) {
                    iosTokensSandboxMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                } else {
                    iosTokensDistMap.put(deviceEntity.getToken(), deviceEntity.getBadgeCount());
                }
            }

        } else {
            List<UserEntity> userEntities = new ArrayList<>();
            notificationReqDto.getUserIds().forEach(userId -> {
                Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                if (optionalUserEntity.get().getDevice() != null && optionalUserEntity.get().getDevice().getToken() != null) {
                    optionalUserEntity.get().getDevice().setBadgeCount(optionalUserEntity.get().getDevice().getBadgeCount() + 1);
                    if (optionalUserEntity.get().getDevice().getPlatform().equals(Platform.ANDROID)) {
                        androidTokenMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                    } else if (optionalUserEntity.get().getDevice().getPlatform().equals(Platform.IOS)) {
                        if (optionalUserEntity.get().getDevice().getIosNotificationMode() != null && optionalUserEntity.get().getDevice().getIosNotificationMode().equals(IOSNotificationMode.SANDBOX)) {
                            iosTokensSandboxMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                        } else {
                            iosTokensDistMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                        }
                    }

                }
                userEntities.add(optionalUserEntity.get());
            });
            notification.setUsers(userEntities);
        }

        Map<String, String> messageData = new HashMap<>();
        messageData.put("title", notificationReqDto.getTitle());
        messageData.put("description", notificationReqDto.getDescription());
        Map<String, String> customFields = new HashMap<>();

        if (androidTokenMap.size() > 0 || iosTokensSandboxMap.size() > 0 || iosTokensDistMap.size() > 0) {
            NotificationEntity notificationEntity = notificationRepo.save(notification);

            customFields.put("id", notificationEntity.getId().toString());

            PushNotificationUtil.sendPushNotification(Platform.ANDROID, androidTokenMap, messageData, customFields, null);
            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensSandboxMap, messageData, customFields, IOSNotificationMode.SANDBOX);
            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensDistMap, messageData, customFields, IOSNotificationMode.DIST);

            return dtoUtil.prepRes(notificationEntity);
        } else {
            throw new CustomException("ERR006");
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    public void sendNotification(NotificationDto notificationDto) throws FirebaseMessagingException, IOException {
        LOG.info("----- Sending Notification. -----");

        if (notificationDto.getUserIds() != null && notificationDto.getUserIds().size() > 0) {
            Map<String, Integer> androidTokenMap = new HashMap<>();
            Map<String, Integer> iosTokensSandboxMap = new HashMap<>();
            Map<String, Integer> iosTokensDistMap = new HashMap<>();

            notificationDto.getUserIds().forEach(userId -> {
                Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

                if (optionalUserEntity.get().getDevice() != null && optionalUserEntity.get().getDevice().getToken() != null) {
                    optionalUserEntity.get().getDevice().setBadgeCount(optionalUserEntity.get().getDevice().getBadgeCount() + 1);
                    if (optionalUserEntity.get().getDevice().getPlatform().equals(Platform.ANDROID)) {
                        androidTokenMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                    } else if (optionalUserEntity.get().getDevice().getPlatform().equals(Platform.IOS)) {
                        if (optionalUserEntity.get().getDevice().getIosNotificationMode() != null && optionalUserEntity.get().getDevice().getIosNotificationMode().equals(IOSNotificationMode.SANDBOX)) {
                            iosTokensSandboxMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                        } else {
                            iosTokensDistMap.put(optionalUserEntity.get().getDevice().getToken(), optionalUserEntity.get().getDevice().getBadgeCount());
                        }
                    }
                }
            });

            Map<String, String> messageData = new HashMap<>();
            messageData.put("title", notificationDto.getTitle());
            messageData.put("description", notificationDto.getDescription());

            PushNotificationUtil.sendPushNotification(Platform.ANDROID, androidTokenMap, messageData, notificationDto.getCustomFields(), null);
            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensSandboxMap, messageData, notificationDto.getCustomFields(), IOSNotificationMode.SANDBOX);
            PushNotificationUtil.sendPushNotification(Platform.IOS, iosTokensDistMap, messageData, notificationDto.getCustomFields(), IOSNotificationMode.DIST);
        }
    }

    public PaginationResDto<NotificationRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Notification of Logged in user. -----");
        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        List<String> fields = Arrays.asList("title", "description","createdDate","dateTime","notificationType" );
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortOrder() != null) {
            if (paginationReqDto.getSortBy().equalsIgnoreCase("notificationType"))
            {
                sortBy = "notificationType";
            }
            else if (paginationReqDto.getSortBy().equalsIgnoreCase("dateTime"))
            {
                sortBy = "createdDate";
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<NotificationEntity> notificationEntityPage = null;
        List<NotificationEntity> notificationEntities;
        if (paginationReqDto.getPageSize() > 0) {
            if (SessionManager.isSuperAdmin()) {
                notificationEntityPage = notificationRepo.findAll(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            } else {
                notificationEntityPage = notificationRepo.findAllByUsersInOrNotificationTypeInOrRolesInAndCreatedDateAfter(
                        Arrays.asList(optionalUserEntity.get()), Arrays.asList(NotificationType.BROADCAST),
                        Arrays.asList(optionalUserEntity.get().getRole()),
                        optionalUserEntity.get().getCreatedDate(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            }
            notificationEntities = notificationEntityPage.getContent();
        } else {
            if (SessionManager.isSuperAdmin()) {
                notificationEntities = notificationRepo.findAll(Sort.by(sortOrder, sortBy));
            } else {
                notificationEntities = notificationRepo.findAllByUsersInOrNotificationTypeInOrRolesInAndCreatedDateAfter(
                        Arrays.asList(optionalUserEntity.get()), Arrays.asList(NotificationType.BROADCAST),
                        Arrays.asList(optionalUserEntity.get().getRole()), optionalUserEntity.get().getCreatedDate(), Sort.by(sortOrder, sortBy));
            }
        }

        List<NotificationRes> notificationResDtos = notificationEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(notificationEntityPage, notificationResDtos);
    }

    public PaginationResDto<NotificationRes> getBroadcasted(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Broadcasted Notification. -----");
        List<String> fields = Arrays.asList("title", "description", "createdDate","dateTime","notificationType");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortOrder() != null) {
            if (paginationReqDto.getSortBy().equalsIgnoreCase("notificationType")) {
                sortBy="Type";
            }
            else if(paginationReqDto.getSortBy().equalsIgnoreCase("dateTime"))
            {
                sortBy="createdDate";
            }
            else {
                sortBy = paginationReqDto.getSortBy();
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<NotificationEntity> notificationEntityPage = null;
        List<NotificationEntity> notificationEntities;
        if (paginationReqDto.getPageSize() > 0) {
            notificationEntityPage = notificationRepo.findAllByNotificationType(NotificationType.BROADCAST, PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            notificationEntities = notificationEntityPage.getContent();
        } else {
            notificationEntities = notificationRepo.findAllByNotificationType(NotificationType.BROADCAST, Sort.by(sortOrder, sortBy));
        }

        List<NotificationRes> notificationResDtos = notificationEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(notificationEntityPage, notificationResDtos);
    }

    @PreAuthorize("hasAuthority('NOTIFICATION_RA')")
    public PaginationResDto<NotificationRes> getByRole(Long roleId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Notification by Role. -----");

        if (!SessionManager.isSuperAdmin()) {
            Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
            optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

            if (!optionalUserEntity.get().getRole().getId().equals(roleId)) {
                throw new CustomException("ERR003");
            }
        }

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleId);
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

        List<String> fields = Arrays.asList("title", "description", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<NotificationEntity> notificationEntityPage = null;
        List<NotificationEntity> notificationEntities;
        if (paginationReqDto.getPageSize() > 0) {
            notificationEntityPage = notificationRepo.findAllByNotificationTypeAndRolesIn(NotificationType.BROADCAST, Arrays.asList(optionalRoleEntity.get()), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            notificationEntities = notificationEntityPage.getContent();
        } else {
            notificationEntities = notificationRepo.findAllByNotificationTypeAndRolesIn(NotificationType.BROADCAST, Arrays.asList(optionalRoleEntity.get()), Sort.by(sortOrder, sortBy));
        }

        List<NotificationRes> notificationResDtos = notificationEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(notificationEntityPage, notificationResDtos);
    }

    public void delete(Long notificationId) {
        LOG.info("----- Deleting Notification permanently. -----");
        Optional<NotificationEntity> optionalNotificationEntity = notificationRepo.findById(notificationId);
        optionalNotificationEntity.orElseThrow(() -> new CustomException("NTI001"));

        notificationRepo.delete(optionalNotificationEntity.get());
    }
}
