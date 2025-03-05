package com.chronelab.riscc.dto.request.general;

import com.chronelab.riscc.enums.Gender;
import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.Platform;
import com.chronelab.riscc.util.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserReqDto {
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    private String mobileNumber;
    private String emailAddress;
    private String image;

    private VerificationReq verification;

    private String deviceToken;
    private Platform devicePlatform;
    private IOSNotificationMode iosNotificationMode;
    private Integer badgeCount;

    private Long roleId;
}
