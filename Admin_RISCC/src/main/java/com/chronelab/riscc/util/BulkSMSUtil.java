package com.chronelab.riscc.util;

import com.chronelab.riscc.entity.SettingEntity;
import com.chronelab.riscc.repo.SettingRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Component
public class BulkSMSUtil implements SMSUtil {

    private static final Logger LOG = LogManager.getLogger();

    private String TOKEN_ID;
    private String TOKEN_SECRET;

    @Value("${bulksms.base.url}")
    private String BASE_URL;

    @Autowired
    private SettingRepo settingRepo;

    public BulkSMSUtil(SettingRepo settingRepo) {
        this.settingRepo = settingRepo;
        this.setBulkSmsParameter();
    }

    @Override
    public boolean sendSMS(String to, String text) {

        LOG.info("----- Sending SMS to " + to + " -----");

        String authEncoded = Base64.getEncoder().encodeToString((TOKEN_ID + ":" + TOKEN_SECRET).getBytes());

        Message message = new Message();
        message.setTo(to).setBody(text);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + authEncoded);

        HttpEntity<Message> request = new HttpEntity<>(message, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, request, String.class, httpHeaders);
        return response.getBody() != null && response.getBody().contains("type") && response.getBody().contains("SENT");
    }

    private void setBulkSmsParameter() {
        List<SettingEntity> settingEntities = settingRepo.findAll();
        for (SettingEntity settingEntity : settingEntities) {
            if (settingEntity.getKeyTitle().equalsIgnoreCase("BULKSMS_TOKEN_ID")) {
                this.TOKEN_ID = settingEntity.getKeyValue();
            }
            if (settingEntity.getKeyTitle().equalsIgnoreCase("BULKSMS_TOKEN_SECRET")) {
                this.TOKEN_SECRET = settingEntity.getKeyValue();
            }
        }
    }

    class Message {
        private String to;
        private String body;

        public String getTo() {
            return to;
        }

        public Message setTo(String to) {
            this.to = to;
            return this;
        }

        public String getBody() {
            return body;
        }

        public Message setBody(String body) {
            this.body = body;
            return this;
        }
    }
}
