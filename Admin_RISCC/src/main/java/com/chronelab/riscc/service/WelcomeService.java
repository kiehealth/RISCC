package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.WelcomeDto;
import com.chronelab.riscc.entity.WelcomeEntity;
import com.chronelab.riscc.enums.LanguageCode;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.WelcomeRepo;
import com.chronelab.riscc.util.CommonValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WelcomeService {

    private static final Logger LOG = LogManager.getLogger();

    private final WelcomeRepo welcomeRepo;
    private final CommonValue commonValue;

    @Autowired
    public WelcomeService(WelcomeRepo welcomeRepo, CommonValue commonValue) {
        this.welcomeRepo = welcomeRepo;
        this.commonValue = commonValue;
    }

    public WelcomeDto get() {
        LOG.info("----- Getting Welcome. -----");
        WelcomeEntity welcomeEntity = welcomeRepo.findAll().get(0);
        return new WelcomeDto()
                .setId(welcomeEntity.getId())
                .setWelcomeText(welcomeEntity.getWelcomeText())
                .setWelcomeTextSwedish(welcomeEntity.getWelcomeTextSwedish())
                .setWelcomeTextSpanish(welcomeEntity.getWelcomeTextSpanish())
                .setThankYouText(welcomeEntity.getThankYouText())
                .setThankYouTextSwedish(welcomeEntity.getThankYouTextSwedish())
                .setThankYouTextSpanish(welcomeEntity.getThankYouTextSpanish());
    }

    public WelcomeDto getByLanguage() {
        LOG.info("----- Getting Welcome by Language. -----");
        WelcomeEntity welcomeEntity = welcomeRepo.findAll().get(0);
        WelcomeDto welcomeDto = new WelcomeDto();
        if (commonValue.getClientLanguageCode().equals(LanguageCode.ENGLISH)) {
            welcomeDto.setWelcomeText(welcomeEntity.getWelcomeText());
            welcomeDto.setThankYouText(welcomeEntity.getThankYouText());
        } else if (commonValue.getClientLanguageCode().equals(LanguageCode.SWEDISH)) {
            welcomeDto.setWelcomeTextSwedish(welcomeEntity.getWelcomeTextSwedish());
            welcomeDto.setThankYouTextSwedish(welcomeEntity.getThankYouTextSwedish());
        } else if (commonValue.getClientLanguageCode().equals(LanguageCode.SPANISH)) {
            welcomeDto.setWelcomeTextSpanish(welcomeEntity.getWelcomeTextSpanish());
            welcomeDto.setThankYouTextSpanish(welcomeEntity.getThankYouTextSpanish());
        }
        return welcomeDto;
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public WelcomeDto update(WelcomeDto welcomeDto) {
        LOG.info("----- Updating Welcome. -----");
        WelcomeEntity welcomeEntity = welcomeRepo.findById(welcomeDto.getId()).orElseThrow(() -> new CustomException("SET002"));
        if (welcomeDto.getWelcomeText() != null && (welcomeEntity.getWelcomeText() == null || !welcomeDto.getWelcomeText().equals(welcomeEntity.getWelcomeText()))) {
            welcomeEntity.setWelcomeText(welcomeDto.getWelcomeText());
        }
        if (welcomeDto.getWelcomeTextSwedish() != null && (welcomeEntity.getWelcomeTextSwedish() == null || !welcomeDto.getWelcomeTextSwedish().equals(welcomeEntity.getWelcomeTextSwedish()))) {
            welcomeEntity.setWelcomeTextSwedish(welcomeDto.getWelcomeTextSwedish());
        }
        if (welcomeDto.getWelcomeTextSpanish() != null && (welcomeEntity.getWelcomeTextSpanish() == null || !welcomeDto.getWelcomeTextSpanish().equals(welcomeEntity.getWelcomeTextSpanish()))) {
            welcomeEntity.setWelcomeTextSpanish(welcomeDto.getWelcomeTextSpanish());
        }
        if (welcomeDto.getThankYouText() != null && (welcomeEntity.getThankYouText() == null || !welcomeDto.getThankYouText().equals(welcomeEntity.getThankYouText()))) {
            welcomeEntity.setThankYouText(welcomeDto.getThankYouText());
        }
        if (welcomeDto.getThankYouTextSwedish() != null && (welcomeEntity.getThankYouTextSwedish() == null || !welcomeDto.getThankYouTextSwedish().equals(welcomeEntity.getThankYouTextSwedish()))) {
            welcomeEntity.setThankYouTextSwedish(welcomeDto.getThankYouTextSwedish());
        }
        if (welcomeDto.getThankYouTextSpanish() != null && (welcomeEntity.getThankYouTextSpanish() == null || !welcomeDto.getThankYouTextSpanish().equals(welcomeEntity.getThankYouTextSpanish()))) {
            welcomeEntity.setThankYouTextSpanish(welcomeDto.getThankYouTextSpanish());
        }

        return new WelcomeDto()
                .setId(welcomeEntity.getId())
                .setWelcomeText(welcomeEntity.getWelcomeText())
                .setWelcomeTextSwedish(welcomeEntity.getWelcomeTextSwedish())
                .setWelcomeTextSpanish(welcomeEntity.getWelcomeTextSpanish())
                .setThankYouText(welcomeEntity.getThankYouText())
                .setThankYouTextSwedish(welcomeEntity.getThankYouTextSwedish())
                .setThankYouTextSpanish(welcomeEntity.getWelcomeTextSpanish());
    }
}
