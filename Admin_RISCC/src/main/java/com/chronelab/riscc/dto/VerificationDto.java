package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationDto {
    private Long id;
    private String emailAddress;
    private String mobileNumber;
    private String verificationCode;
    private boolean emailSent;
    private boolean smsSent;

    public VerificationDto setId(Long id) {
        this.id = id;
        return this;
    }

    public VerificationDto setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public VerificationDto setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public VerificationDto setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public VerificationDto setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
        return this;
    }

    public VerificationDto setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
        return this;
    }
}
