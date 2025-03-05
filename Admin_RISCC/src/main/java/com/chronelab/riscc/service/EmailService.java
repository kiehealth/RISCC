package com.chronelab.riscc.service;

public interface EmailService {
    void sendFormattedEmail(String to, String subject, String body);
}
