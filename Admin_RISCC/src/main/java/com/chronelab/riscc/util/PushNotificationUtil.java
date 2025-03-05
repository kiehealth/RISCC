package com.chronelab.riscc.util;

import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.Platform;
import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PushNotificationUtil {

    private static final Logger LOG = LogManager.getLogger();

    /*public static boolean sendPushNotification(String registrationToken, Map<String, String> messageData) throws FirebaseMessagingException, IOException {
        // See documentation on defining a message payload.
        if (FirebaseApp.getApps().size() == 0) {
            init();
        }
        Message message = Message.builder()
                .setNotification(new Notification(messageData.get("title"), messageData.get("description")))
                //.putAllData(messageData)
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided registration token.
        String response = FirebaseMessaging.getInstance().send(message);

        // Response is a message ID string.
        LOG.info("----- Successfully sent message: " + response + " -----");
        return true;
    }*/

    public static void sendPushNotification(Platform platform, Map<String, Integer> registrationTokens, Map<String, String> messageData, Map<String, String> customFields, IOSNotificationMode iosNotificationMode) throws FirebaseMessagingException, IOException {
        if (registrationTokens != null && registrationTokens.size() > 0) {

            ApnsClient apnsClient = getApnsClient(iosNotificationMode);

            // Create a list containing up to 100 registration tokens.
            // These registration tokens come from the client FCM SDKs.
            /*List<List<String>> splittedTokens = new ArrayList<>();
            if (registrationTokens.size() > 100) {
                int counter = registrationTokens.size();
                int start = 0;
                while (counter > 100) {
                    splittedTokens.add(registrationTokens.subList(start, start + 100));
                    start = 100;
                    counter = counter - 100;
                }
                splittedTokens.add(registrationTokens.subList(start, registrationTokens.size()));
            } else {
                splittedTokens.add(registrationTokens);
            }*/

            if (platform.equals(Platform.ANDROID) && FirebaseApp.getApps().size() == 0) {
                init();
            }

            for (Map.Entry<String, Integer> token : registrationTokens.entrySet()) {
                if (platform.equals(Platform.ANDROID)) {

                    MulticastMessage.Builder builder = MulticastMessage.builder();
                    builder.setNotification(new Notification(messageData.get("title"), messageData.get("description")))
                            .putData("badge", token.getValue().toString())
                            .putData("sound", "default");
                    /*customFields.put("badge", token.getValue().toString());
                    customFields.put("sound", "default");
                    builder.putAllData(customFields);*/

                    MulticastMessage message = builder.addToken(token.getKey()).build();

                    /*MulticastMessage message = MulticastMessage.builder()
                            .setNotification(new Notification(messageData.get("title"), messageData.get("description")))
                            .putData("storyboard", "Chat")
                            .putData("identifier", "ContactVC")
                            //.putAllData(messageData)
                            .addAllTokens(tokens)
                            .build();*/
                    BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

                    // See the BatchResponse reference documentation for the contents of response.
                    LOG.info("-----" + response.getSuccessCount() + " messages were sent successfully (Android)." + " -----");
                } else if (platform.equals(Platform.IOS)) {
                    try {

                        final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
                        payloadBuilder.setAlertTitle(messageData.get("title"))
                                .setAlertBody(messageData.get("description"))
                                .setBadgeNumber(token.getValue())
                                .setSound("default");
                        if (customFields != null && customFields.size() > 0) {
                            for (Map.Entry<String, String> entry : customFields.entrySet()) {
                                payloadBuilder.addCustomProperty(entry.getKey(), entry.getValue());
                            }
                        }

                        String payload = payloadBuilder.build();

                        String tokenSanitized = TokenUtil.sanitizeTokenString(token.getKey());
                        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(tokenSanitized, ConfigUtil.INSTANCE.getIosTopic(), payload);
                        PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(pushNotification).get();
                        LOG.info(response);

                        /*PayloadBuilder builder = APNS.newPayload()
                                .alertTitle(messageData.get("title"))
                                .alertBody(messageData.get("description"))
                                .badge(token.getValue())
                                .sound("default");
                        *//*customFields.put("badge", token.getValue().toString());
                        customFields.put("sound", "default");*//*
                        builder.customFields(customFields);

                        String payload = builder.build();
                        *//*String payload = APNS.newPayload()
                                .customField("storyboard", "Chat")
                                .customField("identifier", "ContactVC")
                                .alertTitle(messageData.get("title"))
                                .alertBody(messageData.get("description"))
                                .build();*//*
                         *//*getAPNsService(ConfigUtil.INSTANCE.getRunningEnvironment().equalsIgnoreCase("Localhost"))
                                .push(tokens, payload);*//*
                        getAPNsService(iosNotificationMode)
                                .push(token.getKey(), payload);*/
                        LOG.info("----- Push Notification sent (IOS). -----");
                    } catch (Exception e) {
                        LOG.info("----- Failed to send Push Notification (IOS). -----");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*public static boolean sendPushNotification(Platform platform, List<String> registrationTokens, Map<String, String> messageData, IOSNotificationMode iosNotificationMode) throws FirebaseMessagingException, IOException {
        return sendPushNotification(platform, registrationTokens, messageData, null, iosNotificationMode);
    }*/

    public static void init() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(ConfigUtil.INSTANCE.getFirebaseAdminSdkFileName());
        FileInputStream serviceAccount = new FileInputStream(classPathResource.getFile());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(ConfigUtil.INSTANCE.getFirebaseDatabaseUrl())
                .build();

        FirebaseApp.initializeApp(options);
    }

    /*private static ApnsService getAPNsService(IOSNotificationMode iosNotificationMode) {

        ApnsService apnsService = null;
        InputStream inputStream;
        try {
            if (iosNotificationMode != null && iosNotificationMode.equals(IOSNotificationMode.SANDBOX)) {
                LOG.info("----- Using p12 for Sandbox. -----");
                inputStream = new ClassPathResource(ConfigUtil.INSTANCE.getIosCertificateNameSandbox()).getInputStream();
                apnsService = APNS.newService().withCert(inputStream, ConfigUtil.INSTANCE.getIosPushNotificationPassphrase()).withSandboxDestination().build();
            } else {
                LOG.info("----- Using p12 for Distribution. -----");
                inputStream = new ClassPathResource(ConfigUtil.INSTANCE.getIosCertificateNameDist()).getInputStream();
                apnsService = APNS.newService().withCert(inputStream, ConfigUtil.INSTANCE.getIosPushNotificationPassphrase()).withProductionDestination().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apnsService;
    }*/

    private static ApnsClient getApnsClient(IOSNotificationMode iosNotificationMode) {
        ApnsClient apnsClient = null;
        InputStream inputStream;
        try {
            if (iosNotificationMode != null && iosNotificationMode.equals(IOSNotificationMode.SANDBOX)) {
                LOG.info("----- Using p12 for Sandbox. -----");
                inputStream = new ClassPathResource(ConfigUtil.INSTANCE.getIosCertificateNameSandbox()).getInputStream();
                apnsClient = new ApnsClientBuilder()
                        .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                        .setClientCredentials(inputStream, ConfigUtil.INSTANCE.getIosPushNotificationPassphrase())
                        .build();
            } else {
                LOG.info("----- Using p12 for Distribution. -----");
                inputStream = new ClassPathResource(ConfigUtil.INSTANCE.getIosCertificateNameDist()).getInputStream();
                apnsClient = new ApnsClientBuilder()
                        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                        .setClientCredentials(inputStream, ConfigUtil.INSTANCE.getIosPushNotificationPassphrase())
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apnsClient;
    }

}
