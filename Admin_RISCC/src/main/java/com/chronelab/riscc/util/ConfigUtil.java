package com.chronelab.riscc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/* Created by: Binay Singh */

@Service
public class ConfigUtil {
    public static ConfigUtil INSTANCE;

    @Value("${riscc.server}")
    private String runningEnvironment;

    @Value("${file.saving.directory}")
    private String fileSavingDirectory;

    @Value("${file.saving.directory.temp}")
    private String fileSavingDirectoryTemp;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${firebase.admin.sdk.file.name}")
    private String firebaseAdminSdkFileName;

    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @Value("${ios.push.notification.passphrase}")
    private String iosPushNotificationPassphrase;

    @Value("${ios.sandbox}")
    private String iosSandbox;

    @Value("${ios.distribution}")
    private String iosDistribution;

    @Value("${ios.topic}")
    private String iosTopic;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public String getRunningEnvironment() {
        return runningEnvironment;
    }

    public String getFileSavingDirectory() {
        return fileSavingDirectory;
        /*ApplicationHome applicationHome = new ApplicationHome(NvsApplication.class);
        return "file:///" + applicationHome.getDir().toString() + "/saved_file/";*/
    }

    public String getFileSavingDirectoryTemp() {
        return fileSavingDirectoryTemp;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public Long getExpiration() {
        return expiration;
    }

    public String getIosCertificateNameSandbox() {
        return iosSandbox;
    }

    public String getIosCertificateNameDist() {
        return iosDistribution;
    }

    public String getFirebaseAdminSdkFileName() {
        return firebaseAdminSdkFileName;
    }

    public String getFirebaseDatabaseUrl() {
        return firebaseDatabaseUrl;
    }

    public String getIosPushNotificationPassphrase() {
        return iosPushNotificationPassphrase;
    }

    public String getIosTopic() {
        return iosTopic;
    }
}
