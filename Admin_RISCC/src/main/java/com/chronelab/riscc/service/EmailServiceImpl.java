package com.chronelab.riscc.service;

import com.chronelab.riscc.util.GmailUtil;
import com.chronelab.riscc.util.SMTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final SMTPUtil smtpUtil;
    private final GmailUtil gmailUtil;
    @Value("${email.mode}")
    private String emailMode;

    @Autowired
    public EmailServiceImpl(SMTPUtil smtpUtil, GmailUtil gmailUtil) {
        this.smtpUtil = smtpUtil;
        this.gmailUtil = gmailUtil;
    }


    @Override
    public void sendFormattedEmail(String to, String subject, String body) {
        if (emailMode.equalsIgnoreCase("SMTP")) {
            smtpUtil.sendFormattedEmail(to, subject, body);
        } else {
            gmailUtil.sendFormattedEmail(to, subject, body);
        }
    }
}
