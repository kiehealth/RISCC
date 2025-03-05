package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.SettingDto;
import com.chronelab.riscc.entity.SettingEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.SettingRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('SETTING')")
@Service
@Transactional(rollbackFor = Exception.class)
public class SettingService {

    private final static Logger LOG = LogManager.getLogger();

    private final SettingRepo settingRepo;

    @Autowired
    public SettingService(SettingRepo settingRepo) {
        this.settingRepo = settingRepo;
    }

    public List<SettingDto> get() {
        LOG.info("----- Getting Settings. -----");
        List<SettingEntity> settingEntities = settingRepo.findAll();
        return settingEntities.stream().map(settingEntity ->
                new SettingDto().setId(settingEntity.getId())
                        .setKeyTitle(settingEntity.getKeyTitle())
                        .setKeyValue(settingEntity.getKeyValue())
        ).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public SettingDto update(SettingDto settingDto) {
        LOG.info("----- Updating Setting. -----");
        Optional<SettingEntity> optionalSettingEntity = settingRepo.findById(settingDto.getId());
        optionalSettingEntity.orElseThrow(() -> new CustomException("SET001"));

        if (settingDto.getKeyValue() != null && !settingDto.getKeyValue().isEmpty()
                && !settingDto.getKeyValue().equalsIgnoreCase(optionalSettingEntity.get().getKeyValue())) {
            optionalSettingEntity.get().setKeyValue(settingDto.getKeyValue());
        }

        return new SettingDto()
                .setId(optionalSettingEntity.get().getId())
                .setKeyTitle(optionalSettingEntity.get().getKeyTitle())
                .setKeyValue(optionalSettingEntity.get().getKeyValue());
    }
}
