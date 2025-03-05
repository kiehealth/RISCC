package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_welcome")
@DynamicInsert
@DynamicUpdate
public class WelcomeEntity extends CommonEntity {

    @Column(name = "welcome_text", columnDefinition = "TEXT")
    private String welcomeText;

    @Column(name = "welcome_text_swedish", columnDefinition = "TEXT")
    private String welcomeTextSwedish;

    @Column(name = "welcome_text_spanish", columnDefinition = "TEXT")
    private String welcomeTextSpanish;

    @Column(name = "thank_you_text", columnDefinition = "TEXT")
    private String thankYouText;

    @Column(name = "thank_you_text_swedish", columnDefinition = "TEXT")
    private String thankYouTextSwedish;

    @Column(name = "thank_you_text_spanish", columnDefinition = "TEXT")
    private String thankYouTextSpanish;

    public WelcomeEntity setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
        return this;
    }

    public WelcomeEntity setWelcomeTextSwedish(String welcomeTextSwedish) {
        this.welcomeTextSwedish = welcomeTextSwedish;
        return this;
    }

    public WelcomeEntity setWelcomeTextSpanish(String welcomeTextSpanish) {
        this.welcomeTextSpanish = welcomeTextSpanish;
        return this;
    }

    public WelcomeEntity setThankYouText(String thankYouText) {
        this.thankYouText = thankYouText;
        return this;
    }

    public WelcomeEntity setThankYouTextSwedish(String thankYouTextSwedish) {
        this.thankYouTextSwedish = thankYouTextSwedish;
        return this;
    }

    public WelcomeEntity setThankYouTextSpanish(String thankYouTextSpanish) {
        this.thankYouTextSpanish = thankYouTextSpanish;
        return this;
    }
}
