package com.chronelab.riscc.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RisccCalculationRes {
    private String calculationType;
    private BigDecimal risccValue;
    private String message;
    private String moreInfo;

    public RisccCalculationRes setCalculationType(String calculationType) {
        this.calculationType = calculationType;
        return this;
    }

    public RisccCalculationRes setRisccValue(BigDecimal risccValue) {
        this.risccValue = risccValue;
        return this;
    }

    public RisccCalculationRes setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    public RisccCalculationRes
    setMessage(String message) {
        this.message = message;
        return this;
    }
}
