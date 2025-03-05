package com.chronelab.riscc.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

public class CalculationUtil {

    public static BigDecimal calculateDiscount(BigDecimal amount, byte percent) {
        if (amount != null) {
            if (percent == 0) {
                return amount;
            }
            return amount.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_DOWN);
        }
        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Amount is not provided to calculate discount from.");
    }
}
