package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.VerificationDto;
import com.chronelab.riscc.dto.request.AppAnalyticsReq;
import com.chronelab.riscc.dto.response.AppAnalyticsRes;
import com.chronelab.riscc.dto.util.AppAnalyticsDtoUtil;
import com.chronelab.riscc.entity.AppAnalyticsEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.VerificationEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.AppAnalyticsKeyDataType;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AllowedRegistrationRepo;
import com.chronelab.riscc.repo.AppAnalyticsKeyRepo;
import com.chronelab.riscc.repo.AppAnalyticsRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.general.VerificationRepo;
import com.chronelab.riscc.util.BulkSMSUtil;
import com.chronelab.riscc.util.EmailMessage;
import com.chronelab.riscc.util.GeneralUtil;
import com.chronelab.riscc.util.SMSUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnonService {

    private static final Logger LOG = LogManager.getLogger();

    private final EmailService emailService;
    private final UserRepo userRepo;
    private final VerificationRepo verificationRepo;
    private final SMSUtil smsUtil;
    private final AppAnalyticsRepo appAnalyticsRepo;
    private final AppAnalyticsKeyRepo appAnalyticsKeyRepo;
    private final AppAnalyticsDtoUtil appAnalyticsDtoUtil;
    private final AllowedRegistrationRepo allowedRegistrationRepo;

    @Autowired
    public AnonService(EmailServiceImpl emailServiceImpl, UserRepo userRepo, VerificationRepo verificationRepo, BulkSMSUtil bulkSMSUtil,
                       AppAnalyticsRepo appAnalyticsRepo, AppAnalyticsKeyRepo appAnalyticsKeyRepo, AppAnalyticsDtoUtil appAnalyticsDtoUtil, AllowedRegistrationRepo allowedRegistrationRepo) {
        this.emailService = emailServiceImpl;
        this.userRepo = userRepo;
        this.verificationRepo = verificationRepo;
        this.smsUtil = bulkSMSUtil;
        this.appAnalyticsRepo = appAnalyticsRepo;
        this.appAnalyticsKeyRepo = appAnalyticsKeyRepo;
        this.appAnalyticsDtoUtil = appAnalyticsDtoUtil;
        this.allowedRegistrationRepo = allowedRegistrationRepo;
    }

    public VerificationDto sendVerificationCode(VerificationDto verificationDto) {
        LOG.info("----- Saving verification. -----");
        if (verificationDto.getEmailAddress() == null && verificationDto.getMobileNumber() == null) {
            throw new IllegalArgumentException("Please provide either email address or mobile number.");
        }
        VerificationEntity verification;
        String verificationCode = GeneralUtil.generateRandomDigit();
        String encodedVerificationCode = GeneralUtil.sha256(verificationCode);
        verificationDto.setVerificationCode(verificationCode);

        if (verificationDto.getEmailAddress() != null && verificationDto.getMobileNumber() != null) {

            if (!allowedRegistrationRepo.findByEmail(verificationDto.getEmailAddress()).isPresent()) {
                throw new CustomException("VER006");
            }

            if (userRepo.findByEmailAddressAndMobileNumber(verificationDto.getEmailAddress(), verificationDto.getMobileNumber()) != null) {
                throw new CustomException("USR002");
            }
            if (userRepo.findByEmailAddress(verificationDto.getEmailAddress()) != null) {
                throw new CustomException("USR007");
            }
            if (userRepo.findByMobileNumber(verificationDto.getMobileNumber()) != null) {
                throw new CustomException("USR008");
            }

            verification = verificationRepo.findByEmailAddressAndMobileNumber(verificationDto.getEmailAddress(), verificationDto.getMobileNumber());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setEmailAddress(verificationDto.getEmailAddress())
                        .setMobileNumber(verificationDto.getMobileNumber())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            String body = EmailMessage.signupVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            //emailUtil.sendGmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            emailService.sendFormattedEmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            verificationDto.setEmailSent(true);

            try {
                if (smsUtil.sendSMS(verificationDto.getMobileNumber(), "Verification Code: " + verificationCode)) {
                    verificationDto.setSmsSent(true);
                } else {
                    throw new CustomException("SMS001");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (verificationDto.getEmailAddress() != null) {

            if (!allowedRegistrationRepo.findByEmail(verificationDto.getEmailAddress()).isPresent()) {
                throw new CustomException("VER006");
            }

            if (userRepo.findByEmailAddress(verificationDto.getEmailAddress()) != null) {
                throw new CustomException("USR002");
            }
            verification = verificationRepo.findByEmailAddress(verificationDto.getEmailAddress());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setEmailAddress(verificationDto.getEmailAddress())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            String body = EmailMessage.signupVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            //emailUtil.sendGmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            emailService.sendFormattedEmail(verificationDto.getEmailAddress(), "Signup Verification Code", body);
            verificationDto.setEmailSent(true);
        } else {
            if (userRepo.findByMobileNumber(verificationDto.getMobileNumber()) != null) {
                throw new CustomException("USR002");
            }
            verification = verificationRepo.findByMobileNumber(verificationDto.getMobileNumber());
            if (verification == null) {
                verification = new VerificationEntity()
                        .setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setMobileNumber(verificationDto.getMobileNumber())
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(1);
                verificationRepo.save(verification);
            } else {
                verification.setDeadline(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(30))
                        .setVerificationCode(encodedVerificationCode)
                        .setVerificationCodeSentCount(verification.getVerificationCodeSentCount() + 1);
            }
            if (smsUtil.sendSMS(verificationDto.getMobileNumber(), "Verification Code: " + verificationCode)) {
                verificationDto.setSmsSent(true);
            } else {
                throw new CustomException("SMS001");
            }
        }

        return verificationDto.setId(verification.getId());
    }

    public String sendVerificationCode(String identity) {
        LOG.info("----- Sending verification code to the user. -----");
        if (identity == null || identity.isEmpty()) {
            throw new CustomException("VER007");
        }
        if (identity.contains("@")) {
            UserEntity userEntity = userRepo.findByEmailAddress(identity);
            if (userEntity == null) {
                throw new CustomException("USR001");
            }
            String verificationCode = GeneralUtil.generateRandomDigit();
            String encodedVerificationCode = GeneralUtil.sha256(verificationCode);

            userEntity.setVerificationCode(encodedVerificationCode);
            userEntity.setVerificationCodeDeadline(LocalDateTime.now().plusMinutes(30));

            String body = EmailMessage.passwordResetVerificationCode(verificationCode);
            //emailUtil.sendFormattedEmail(identity, "Password Reset Verification Code", body);
            //emailUtil.sendGmail(identity, "Password Reset Verification Code", body);
            emailService.sendFormattedEmail(identity, "Password Reset Verification Code", body);
            return verificationCode;

        } else if (identity.length() > 10) {
            UserEntity userEntity = userRepo.findByMobileNumber(identity);
            if (userEntity == null) {
                throw new CustomException("USR001");
            }
            String verificationCode = GeneralUtil.generateRandomDigit();
            String encodedVerificationCode = GeneralUtil.sha256(verificationCode);
            userEntity.setVerificationCode(encodedVerificationCode);
            userEntity.setVerificationCodeDeadline(LocalDateTime.now().plusMinutes(30));
            if (smsUtil.sendSMS(identity, "Verification Code: " + verificationCode)) {
                return verificationCode;
            } else {
                throw new CustomException("SMS001");
            }
        } else {
            throw new IllegalArgumentException("Please provide either email address or mobile number.");
        }
    }

    public void resetPassword(String verificationCode, String newPassword) {
        LOG.info("----- Resetting password. -----");

        UserEntity userEntity = userRepo.findByVerificationCodeAndVerificationCodeDeadlineAfter(GeneralUtil.sha256(verificationCode), LocalDateTime.now());
        if (userEntity == null) {
            throw new CustomException("VER002");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new CustomException("VER008");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        userEntity.setPassword(encodedPassword);
        userEntity.setLastPasswordResetDate(LocalDateTime.now());
        userEntity.setVerificationCode("");
        if (userEntity.getEmailAddress() != null) {
            //emailUtil.sendEmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
            //emailUtil.sendGmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
            emailService.sendFormattedEmail(userEntity.getEmailAddress(), "Password Reset.", "You have successfully reset your password.");
        }
    }

    public AppAnalyticsRes saveAppAnalytics(AppAnalyticsReq appAnalyticsReqDto) {
        LOG.info("----- Saving App Analytics. -----");

        AppAnalyticsEntity appAnalytics = appAnalyticsDtoUtil.reqToEntity(appAnalyticsReqDto);

        Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyEntity = appAnalyticsKeyRepo.findById(appAnalyticsReqDto.getAppAnalyticsKeyId());
        optionalAppAnalyticsKeyEntity.orElseThrow(() -> new CustomException("APA001"));

        appAnalytics.setAppAnalyticsKey(optionalAppAnalyticsKeyEntity.get());

        if (optionalAppAnalyticsKeyEntity.get().getAppAnalyticsKeyDataType().equals(AppAnalyticsKeyDataType.DATETIME)) {
            LocalDateTime localDateTime = Instant.ofEpochMilli(Long.parseLong(appAnalyticsReqDto.getValue())).atZone(ZoneOffset.UTC).toLocalDateTime();
            appAnalytics.setKeyValueDateTime(localDateTime);
        } else if (optionalAppAnalyticsKeyEntity.get().getAppAnalyticsKeyDataType().equals(AppAnalyticsKeyDataType.NUMBER)) {
            appAnalytics.setKeyValueInt(Integer.parseInt(appAnalyticsReqDto.getValue()));
        } else {
            appAnalytics.setKeyValueText(appAnalyticsReqDto.getValue());
        }

        if (!SessionManager.isAnonymousUser()) {
            Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
            optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
            appAnalytics.setUser(optionalUserEntity.get());
        }

        return appAnalyticsDtoUtil.prepRes(appAnalyticsRepo.save(appAnalytics));
    }
}
