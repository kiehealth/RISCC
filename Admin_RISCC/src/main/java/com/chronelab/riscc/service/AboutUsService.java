package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.AboutUsDto;
import com.chronelab.riscc.entity.AboutUsEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AboutUsRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AboutUsService {

    private static final Logger LOG = LogManager.getLogger();

    private final AboutUsRepo aboutUsRepo;

    @Autowired
    public AboutUsService(AboutUsRepo aboutUsRepo) {
        this.aboutUsRepo = aboutUsRepo;
    }

    public AboutUsDto get() {
        LOG.info("----- Getting About Us. -----");
        AboutUsEntity aboutUsEntity = aboutUsRepo.findAll().get(0);
        return new AboutUsDto()
                .setId(aboutUsEntity.getId())
                .setName(aboutUsEntity.getName())
                .setPhone(aboutUsEntity.getPhone())
                .setEmail(aboutUsEntity.getEmail());
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public AboutUsDto update(AboutUsDto aboutUsDto) {
        LOG.info("----- Updating About Us. -----");
        Optional<AboutUsEntity> optionalAboutUsEntity = aboutUsRepo.findById(aboutUsDto.getId());
        optionalAboutUsEntity.orElseThrow(() -> new CustomException("ABT001"));
        AboutUsEntity aboutUsEntity = optionalAboutUsEntity.get();
        if (aboutUsDto.getName() != null && !aboutUsDto.getName().equals(aboutUsEntity.getName())) {
            aboutUsEntity.setName(aboutUsDto.getName());
        }
        if (aboutUsDto.getPhone() != null && !aboutUsDto.getPhone().equals(aboutUsEntity.getPhone())) {
            aboutUsEntity.setPhone(aboutUsDto.getPhone());
        }
        if (aboutUsDto.getEmail() != null && !aboutUsDto.getEmail().equals(aboutUsEntity.getEmail())) {
            aboutUsEntity.setEmail(aboutUsDto.getEmail());
        }

        return new AboutUsDto()
                .setId(aboutUsEntity.getId())
                .setName(aboutUsEntity.getName())
                .setPhone(aboutUsEntity.getPhone())
                .setEmail(aboutUsEntity.getEmail());
    }
}
