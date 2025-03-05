package com.chronelab.riscc.util;

import com.chronelab.riscc.enums.LanguageCode;
import org.springframework.stereotype.Component;

@Component
public class CommonValue {
    private LanguageCode clientLanguageCode = LanguageCode.ENGLISH;

    public LanguageCode getClientLanguageCode() {
        return clientLanguageCode;
    }

    public void setClientLanguageCode(LanguageCode clientLanguageCode) {
        this.clientLanguageCode = clientLanguageCode;
    }
}
