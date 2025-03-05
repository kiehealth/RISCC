package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.PasswordReqDto;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.AllowedRegistrationEntity;
import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.VerificationEntity;
import com.chronelab.riscc.entity.general.DeviceEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.*;
import com.chronelab.riscc.repo.general.RoleRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.general.VerificationRepo;
import com.chronelab.riscc.repo.projection.UserProjection;
import com.chronelab.riscc.repo.specification.SearchCriteria;
import com.chronelab.riscc.repo.specification.UserSpecificationBuilder;
import com.chronelab.riscc.service.general.UserService;
import com.chronelab.riscc.util.FileUtil;
import com.chronelab.riscc.util.GeneralUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger();
    private static final String USER_IMAGE_DIRECTORY = "user_image";

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final VerificationRepo verificationRepo;
    private final DtoUtil<UserEntity, UserReqDto, UserResDto> dtoUtil;
    private final PaginationDtoUtil<UserEntity, UserReqDto, UserResDto> paginationDtoUtil;

    private final NotificationRepo notificationRepo;
    private final AppAnalyticsRepo appAnalyticsRepo;
    private final AnswerRepo answerRepo;
    private final GroupRepo groupRepo;
    private final NoteRepo noteRepo;
    private final AllowedRegistrationRepo allowedRegistrationRepo;

    private final List<String> fields = Arrays.asList("firstName", "lastName", "gender", "dateOfBirth", "mobileNumber", "emailAddress");
    private String sortBy = "firstName";//Default sortBy
    private Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder


    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, VerificationRepo verificationRepo, DtoUtil<UserEntity, UserReqDto, UserResDto> dtoUtil, PaginationDtoUtil<UserEntity, UserReqDto, UserResDto> paginationDtoUtil, NotificationRepo notificationRepo, AppAnalyticsRepo appAnalyticsRepo, AnswerRepo answerRepo, GroupRepo groupRepo, NoteRepo noteRepo, AllowedRegistrationRepo allowedRegistrationRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.verificationRepo = verificationRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.dtoUtil = dtoUtil;
        this.notificationRepo = notificationRepo;
        this.appAnalyticsRepo = appAnalyticsRepo;
        this.answerRepo = answerRepo;
        this.groupRepo = groupRepo;
        this.noteRepo = noteRepo;
        this.allowedRegistrationRepo = allowedRegistrationRepo;
    }

    @Override
    public UserResDto save(UserReqDto userReqDto) throws IOException {
        LOG.info("----- Saving User. -----");

        UserEntity user = dtoUtil.reqToEntity(userReqDto).setLastPasswordResetDate(LocalDateTime.now());
        user.setPassword(new BCryptPasswordEncoder().encode(userReqDto.getPassword()));

        VerificationEntity verificationEntity = null;

        if (!SessionManager.isSuperAdmin()) {
            if (userReqDto.getVerification() == null || userReqDto.getVerification().getId() == null
                    || userReqDto.getVerification().getVerificationCode() == null) {
                throw new CustomException("VER001");
            }

            verificationEntity = verificationRepo.findByIdAndVerificationCodeAndDeadlineAfter(
                    userReqDto.getVerification().getId(),
                    GeneralUtil.sha256(userReqDto.getVerification().getVerificationCode()),
                    LocalDateTime.now(ZoneOffset.UTC)
            );
            if (verificationEntity == null) {
                throw new CustomException("VER002");
            }

            if (userReqDto.getVerification().getEmailAddress() != null
                    && !userReqDto.getEmailAddress().equals(userReqDto.getVerification().getEmailAddress())) {
                throw new CustomException("VER003");
            }
            if (userReqDto.getVerification().getMobileNumber() != null
                    && !userReqDto.getMobileNumber().equals(userReqDto.getVerification().getMobileNumber())) {
                throw new CustomException("VER004");
            }

            if (verificationEntity.getEmailAddress() != null) {

                Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findByEmail(userReqDto.getEmailAddress());
                if (!optionalAllowedRegistrationEntity.isPresent()) {
                    throw new CustomException("VER006");
                }
                user.setEmailAddressVerified(true);
                optionalAllowedRegistrationEntity.get().setRegisteredDateTime(LocalDateTime.now(ZoneOffset.UTC));
            }
            if (verificationEntity.getMobileNumber() != null) {
                user.setMobileNumberVerified(true);
            }

            Optional<RoleEntity> optionalRoleEntity = roleRepo.findByTitle("Participant");
            optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

            user.setRole(optionalRoleEntity.get());

        } else {
            if (userReqDto.getEmailAddress() != null) {
                Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findByEmail(userReqDto.getEmailAddress());
                if (!optionalAllowedRegistrationEntity.isPresent()) {
                    throw new CustomException("VER006");
                }
                user.setEmailAddressVerified(true);
                optionalAllowedRegistrationEntity.get().setRegisteredDateTime(LocalDateTime.now(ZoneOffset.UTC));
            }
            if (userReqDto.getMobileNumber() != null) {
                user.setMobileNumberVerified(true);
            }

            RoleEntity roleEntity;
            if (userReqDto.getRoleId() != null) {
                Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(userReqDto.getRoleId());
                optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
                roleEntity = optionalRoleEntity.get();
            } else {
                Optional<RoleEntity> optionalRoleEntity = roleRepo.findByTitle("Participant");
                optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
                roleEntity = optionalRoleEntity.get();
            }
            user.setRole(roleEntity);
        }

        if (userReqDto.getEmailAddress() == null || userReqDto.getMobileNumber() == null) {
            throw new CustomException("VER005");
        }

        if (userRepo.findByEmailAddress(userReqDto.getEmailAddress()) != null) {
            throw new CustomException("USR005");
        }
        if (userRepo.findByMobileNumber(userReqDto.getMobileNumber()) != null) {
            throw new CustomException("USR006");
        }

        if (userReqDto.getDeviceToken() != null && userReqDto.getDevicePlatform() != null) {
            DeviceEntity device = new DeviceEntity()
                    .setToken(userReqDto.getDeviceToken())
                    .setPlatform(userReqDto.getDevicePlatform())
                    .setBadgeCount(0)
                    .setUser(user);
            if (userReqDto.getIosNotificationMode() != null) {
                device.setIosNotificationMode(userReqDto.getIosNotificationMode());
            }
            user.setDevice(device);
        }
        Optional<GroupEntity> optionalGroupEntity = groupRepo.findByTitle("RISCC DEFAULT");
        optionalGroupEntity.orElseThrow(() -> new CustomException("GRP002"));
        GroupEntity groupEntity = optionalGroupEntity.get();

        UserEntity userEntity = userRepo.save(user);
        groupEntity.getUsers().add(userEntity);

        if (userReqDto.getImage() != null) {
            String imageUrl = FileUtil.saveFile(userReqDto.getImage(), USER_IMAGE_DIRECTORY, userEntity.getId().toString());
            userEntity.setImageUrl(imageUrl);
        }

        if (verificationEntity != null) {
            verificationRepo.delete(verificationEntity);
        }

        return dtoUtil.prepRes(userEntity);
    }

    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<UserResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Users. -----");

        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null) {
            if (paginationReqDto.getSortBy().equals("name")) {
                sortBy = "firstName";
            } else {
                sortBy = paginationReqDto.getSortBy();
            }
        }
        if (paginationReqDto.getSortOrder() != null && paginationReqDto.getSortOrder().equalsIgnoreCase("DESC")) {
            sortOrder = Sort.Direction.ASC;
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (paginationReqDto.getSearchTerm() != null) {
            searchCriteriaList.add(new SearchCriteria("firstName", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("lastName", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("mobileNumber", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("emailAddress", ":", paginationReqDto.getSearchTerm(), true));
        }

        Page<UserEntity> pageUserEntity = null;
        List<UserEntity> userEntities;
        if (paginationReqDto.getPageSize() > 0) {
            pageUserEntity = userRepo.findAll(new UserSpecificationBuilder().with(searchCriteriaList).build(),
                    PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAll(new UserSpecificationBuilder().with(searchCriteriaList).build(), Sort.by(sortOrder, sortBy));
        }

        List<UserResDto> userResDtos = userEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(pageUserEntity, userResDtos);
    }

    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<UserResDto> getByRole(PaginationReqDto paginationReqDto, Long roleId) {
        LOG.info("----- Getting paginated User by Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleId);
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserEntity> pageUserEntity = null;
        List<UserEntity> userEntities;
        if (paginationReqDto.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByRole(optionalRoleEntity.get(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByRole(optionalRoleEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<UserResDto> userResDtos = userEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(pageUserEntity, userResDtos);
    }


    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<UserProjection> getByRoleTitles(PaginationReqDto paginationReqDto, List<String> roleTitles) {
        LOG.info("----- Getting paginated User by Role Titles. -----");

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserProjection> pageUserEntity = null;
        List<UserProjection> userEntities;
        if (paginationReqDto.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByRole_TitleIn(roleTitles, PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByRole_TitleIn(roleTitles, Sort.by(sortOrder, sortBy));
        }

        PaginationResDto<UserProjection> paginationResDto = new PaginationResDto<>();

        if (pageUserEntity != null) {
            paginationResDto
                    .setStartPosition(pageUserEntity.getNumber() * pageUserEntity.getSize() + 1)
                    .setEndPosition((pageUserEntity.getNumber() * pageUserEntity.getSize() + 1) + (pageUserEntity.getContent().size() - 1))
                    .setTotalRecord(pageUserEntity.getTotalElements())
                    .setTotalPage(pageUserEntity.getTotalPages())
                    .setPageSize(pageUserEntity.getSize())
                    .setCurrentPage(pageUserEntity.getNumber() + 1);
        }
        return paginationResDto.setData(userEntities);
    }


    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<UserProjection> getLimited(List<String> fields, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated User by Role Titles. -----");

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserProjection> pageUserEntity = null;
        List<UserProjection> userEntities;
        if (paginationReqDto.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByEmailAddressNotNull(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByEmailAddressNotNull(Sort.by(sortOrder, sortBy));
        }

        PaginationResDto<UserProjection> paginationResDto = new PaginationResDto<>();

        if (pageUserEntity != null) {
            paginationResDto
                    .setStartPosition(pageUserEntity.getNumber() * pageUserEntity.getSize() + 1)
                    .setEndPosition((pageUserEntity.getNumber() * pageUserEntity.getSize() + 1) + (pageUserEntity.getContent().size() - 1))
                    .setTotalRecord(pageUserEntity.getTotalElements())
                    .setTotalPage(pageUserEntity.getTotalPages())
                    .setPageSize(pageUserEntity.getSize())
                    .setCurrentPage(pageUserEntity.getNumber() + 1);
        }
        return paginationResDto.setData(userEntities);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Transactional(readOnly = true)
    @Override
    public UserResDto get() {
        LOG.info("----- Getting User. -----");
        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        return dtoUtil.prepRes(optionalUserEntity.get());
    }

    @PreAuthorize("hasAuthority('USER_U')")
    @Override
    public UserResDto update(UserReqDto userReqDto) throws IOException {
        LOG.info("----- Updating User. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(userReqDto.getId() != null ? userReqDto.getId() : SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        UserEntity userEntity = optionalUserEntity.get();

        if (SessionManager.isSuperAdmin() && userReqDto.getRoleId() != null) {
            Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(userReqDto.getRoleId());
            optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
            userEntity.setRole(optionalRoleEntity.get());
        }

        if (userReqDto.getEmailAddress() != null && !userReqDto.getEmailAddress().equalsIgnoreCase(userEntity.getEmailAddress())
                && userRepo.findByEmailAddress(userReqDto.getEmailAddress()) != null) {
            throw new CustomException("USR002");
        }

        if (userReqDto.getMobileNumber() != null && !userReqDto.getMobileNumber().equalsIgnoreCase(userEntity.getMobileNumber())
                && userRepo.findByMobileNumber(userReqDto.getMobileNumber()) != null) {
            throw new CustomException("USR002");
        }

        if (userReqDto.getImage() != null && userReqDto.getImage().startsWith("data:image/")) {
            if (userEntity.getImageUrl() != null) {
                FileUtil.deleteFile(FileUtil.getAbsolutePathFromFileUrl(userEntity.getImageUrl()));
            }
            String imageUrl = FileUtil.saveFile(userReqDto.getImage(), USER_IMAGE_DIRECTORY, userEntity.getId().toString() + "_" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            userEntity.setImageUrl(imageUrl);
        }

        //Update Device
        if (optionalUserEntity.get().getDevice() == null) {
            DeviceEntity device = new DeviceEntity()
                    .setUser(optionalUserEntity.get())
                    .setUser(optionalUserEntity.get());
            device.setBadgeCount(0);
            if (userReqDto.getIosNotificationMode() != null) {
                device.setIosNotificationMode(userReqDto.getIosNotificationMode());
            }
            if (userReqDto.getDeviceToken() != null) {
                device.setToken(userReqDto.getDeviceToken());
            }
            if (userReqDto.getIosNotificationMode() != null) {
                device.setIosNotificationMode(userReqDto.getIosNotificationMode());
            }
            if (userReqDto.getDevicePlatform() != null) {
                device.setPlatform(userReqDto.getDevicePlatform());
            }
            optionalUserEntity.get().setDevice(device);
        } else {
            if (userReqDto.getDeviceToken() != null && (optionalUserEntity.get().getDevice().getToken() == null ||
                    !userReqDto.getDeviceToken().equalsIgnoreCase(optionalUserEntity.get().getDevice().getToken()))) {
                optionalUserEntity.get().getDevice().setToken(userReqDto.getDeviceToken());
            }
            if (userReqDto.getIosNotificationMode() != null && (optionalUserEntity.get().getDevice().getIosNotificationMode() == null ||
                    !userReqDto.getIosNotificationMode().equals(optionalUserEntity.get().getDevice().getIosNotificationMode()))) {
                optionalUserEntity.get().getDevice().setIosNotificationMode(userReqDto.getIosNotificationMode());
            }
            if (userReqDto.getDevicePlatform() != null && (optionalUserEntity.get().getDevice().getPlatform() == null ||
                    !userReqDto.getDevicePlatform().equals(optionalUserEntity.get().getDevice().getPlatform()))) {
                optionalUserEntity.get().getDevice().setPlatform(userReqDto.getDevicePlatform());
            }
            optionalUserEntity.get().getDevice().setBadgeCount(0);
        }

        dtoUtil.setUpdatedValue(userReqDto, userEntity);
        return dtoUtil.prepRes(userEntity);
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    @Override
    public boolean changePassword(PasswordReqDto passwordReqDto) {
        LOG.info("----- Changing password. -----");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserEntity userEntity;
        if (passwordReqDto.getUserId() != null) {
            Optional<UserEntity> optionalUserEntity = userRepo.findById(passwordReqDto.getUserId());
            optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
            userEntity = optionalUserEntity.get();
        } else {
            userEntity = userRepo.findByEmailAddress(passwordReqDto.getEmailAddress());
        }

        if (userEntity == null) {
            throw new CustomException("USR001");
        }
        if (!userEntity.getId().equals(SessionManager.getUserId())) {
            throw new CustomException("USR003");
        }
        if (!GeneralUtil.matchPassword(passwordReqDto.getOldPassword(), userEntity.getPassword())) {
            throw new CustomException("ERR002");
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(passwordReqDto.getNewPassword()));
        userEntity.setLastPasswordResetDate(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
        return true;
    }

    @PreAuthorize("hasAuthority('USER_D')")
    @Override
    public void delete(Long userId) {
        LOG.info("----- Deleting User permanently. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
        if (optionalUserEntity.get().getRole().getTitle().equalsIgnoreCase("Super Admin")) {
            throw new CustomException("ERR005");
        }

        //Notification
        if (optionalUserEntity.get().getNotifications() != null) {
            notificationRepo.deleteAll(optionalUserEntity.get().getNotifications());
        }

        //User Answer
        answerRepo.deleteAllByUser(optionalUserEntity.get());

        //App Analytics
        appAnalyticsRepo.deleteAllByUser(optionalUserEntity.get());

        //Group
        groupRepo.deleteUserGroupByUserId(optionalUserEntity.get().getId());
        //Group
        /*List<GroupEntity> groupEntities = optionalUserEntity.get().getGroups();
        if (groupEntities != null) {
            groupEntities.forEach(groupEntity -> {
                groupEntity.getUsers().remove(optionalUserEntity.get());
            });
        }*/

        //Note
        noteRepo.deleteAllByUser(optionalUserEntity.get());

        userRepo.delete(optionalUserEntity.get());
    }

    @Override
    public void logout() {
        LOG.info("----- Logging out user by marking loggedout. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        if (optionalUserEntity.get().getDevice() == null) {
            throw new CustomException("USR010");
        }
        optionalUserEntity.get().getDevice().setLoggedOut(true);
    }
}
