package com.chronelab.riscc;

import com.chronelab.riscc.util.GmailTokenHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "utcDateTimeProvider")
@EnableScheduling
public class RISCCApplication {

    public static void main(String[] args) {
        SpringApplication.run(RISCCApplication.class, args);
    }

    @Bean
    public GmailTokenHolder gmailTokenHolder() {
        return new GmailTokenHolder();
    }

    @Bean
    public DateTimeProvider utcDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(ZoneOffset.UTC));
    }
}
