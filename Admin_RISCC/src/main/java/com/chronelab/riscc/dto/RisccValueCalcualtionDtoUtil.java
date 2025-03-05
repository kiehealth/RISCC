package com.chronelab.riscc.dto;

import com.chronelab.riscc.dto.response.AnswerOptionRes;
import com.chronelab.riscc.dto.response.AnswerRes;
import com.chronelab.riscc.dto.response.RisccCalculationRes;
import com.chronelab.riscc.repo.RisccRangeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class RisccValueCalcualtionDtoUtil {

    private final RisccRangeRepo risccRangeRepo;

    @Autowired
    public RisccValueCalcualtionDtoUtil(RisccRangeRepo risccRangeRepo) {
        this.risccRangeRepo = risccRangeRepo;
    }

    public List<RisccCalculationRes> calculateRisccValue(List<AnswerRes> answerResDtos) {
        List<RisccCalculationRes> risccCalculationRes = new ArrayList<>();
        BigDecimal risccValue = new BigDecimal(0);
        for (AnswerRes answerRes : answerResDtos) {
            if (answerRes.getAnswerOptions() != null)
                for (AnswerOptionRes answerOptionRes : answerRes.getAnswerOptions()) {
                    if (answerOptionRes.getRisccValue() != null) {
                        BigDecimal risccBigDecimal = new BigDecimal(answerOptionRes.getRisccValue());
                        risccValue = risccValue.add(risccBigDecimal);
                    }
                }
        }
        RisccCalculationRes risccCalculation = new RisccCalculationRes();
        risccCalculation.setCalculationType("Total RISCC value : ")
                .setRisccValue(risccValue)
                .setMessage(getRisccMessage(risccValue))
                 .setMoreInfo(getMoreinformation(risccValue));

        risccCalculationRes.add(risccCalculation);
        return risccCalculationRes;
    }

    private String getMoreinformation(BigDecimal risccValue) {
        return risccRangeRepo.findMoreInfoByRisccValue(risccValue);
    }

    private String getRisccMessage(BigDecimal risccValue) {
        return risccRangeRepo.findMessageByRisccValue(risccValue);
    }


}
