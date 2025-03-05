package com.chronelab.riscc.dto;

import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.Platform;
import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String deviceToken;
    private Platform devicePlatform;
    private IOSNotificationMode iosNotificationMode;
}
