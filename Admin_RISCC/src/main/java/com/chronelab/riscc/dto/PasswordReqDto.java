package com.chronelab.riscc.dto;

import lombok.Data;

@Data
public class PasswordReqDto {
    private Long userId;
    private String verificationCode;
    private String emailAddress;
    private String oldPassword;
    private String newPassword;
}
