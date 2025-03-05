package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WelcomeDto {
    private Long id;
    private String welcomeText;
    private String welcomeTextSwedish;
    private String welcomeTextSpanish;
    private String thankYouText;
    private String thankYouTextSwedish;
    private String thankYouTextSpanish;

    public WelcomeDto setId(Long id) {
        this.id = id;
        return this;
    }

    public WelcomeDto setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
        return this;
    }

    public WelcomeDto setWelcomeTextSwedish(String welcomeTextSwedish) {
        this.welcomeTextSwedish = welcomeTextSwedish;
        return this;
    }

    public WelcomeDto setWelcomeTextSpanish(String welcomeTextSpanish) {
        this.welcomeTextSpanish = welcomeTextSpanish;
        return this;
    }

    public WelcomeDto setThankYouText(String thankYouText) {
        this.thankYouText = thankYouText;
        return this;
    }

    public WelcomeDto setThankYouTextSwedish(String thankYouTextSwedish) {
        this.thankYouTextSwedish = thankYouTextSwedish;
        return this;
    }

    public WelcomeDto setThankYouTextSpanish(String thankYouTextSpanish) {
        this.thankYouTextSpanish = thankYouTextSpanish;
        return this;
    }
}
