package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.request.ResourceFileReq;
import com.chronelab.riscc.dto.response.ResourceFileRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.ResourceFileDtoUtil;
import com.chronelab.riscc.entity.ResourceFileEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.ResourceFileRepo;
import com.chronelab.riscc.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceFileService {

    private static final Logger LOG = LogManager.getLogger();

    private final ResourceFileRepo resourceFileRepo;
    private final DtoUtil<ResourceFileEntity, ResourceFileReq, ResourceFileRes> dtoUtil;

    @Autowired
    public ResourceFileService(ResourceFileRepo resourceFileRepo, ResourceFileDtoUtil resourceFileDtoUtil) {
        this.resourceFileRepo = resourceFileRepo;
        this.dtoUtil = resourceFileDtoUtil;
    }

    @PreAuthorize("hasAuthority('SETTING')")
    public ResourceFileRes save(ResourceFileReq resourceFileReqDto) throws IOException {
        LOG.info("----- Saving Resource File. -----");

        ResourceFileEntity resourceFile = dtoUtil.reqToEntity(resourceFileReqDto);

        if (resourceFileReqDto.getResourceFile() != null) {
            if (!FileUtil.getMultipartFileExtension(resourceFileReqDto.getResourceFile()).equalsIgnoreCase("pdf")) {
                throw new CustomException("RES001");
            }
            String fileUrl = FileUtil.saveMultipartFile(resourceFileReqDto.getResourceFile(), String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()), "resource_file");
            resourceFile.setUrl(fileUrl);
        }
        return dtoUtil.prepRes(resourceFileRepo.save(resourceFile));
    }

    public List<ResourceFileRes> get() {
        LOG.info("----- Getting Resource File. -----");

        List<ResourceFileEntity> resourceFileEntities = resourceFileRepo.findAll();

        return resourceFileEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public ResourceFileRes update(ResourceFileReq resourceFileReqDto) throws IOException {
        LOG.info("----- Updating Resource File. -----");

        Optional<ResourceFileEntity> optionalResourceFileEntity = resourceFileRepo.findById(resourceFileReqDto.getId());
        optionalResourceFileEntity.orElseThrow(() -> new CustomException("RES002"));

        ResourceFileEntity resourceFileEntity = optionalResourceFileEntity.get();

        dtoUtil.setUpdatedValue(resourceFileReqDto, resourceFileEntity);

        if (resourceFileReqDto.getResourceFile() != null) {
            if (!FileUtil.getMultipartFileExtension(resourceFileReqDto.getResourceFile()).equalsIgnoreCase("pdf")) {
                throw new CustomException("RES001");
            }

            FileUtil.deleteFile(resourceFileEntity.getUrl());

            String fileUrl = FileUtil.saveMultipartFile(resourceFileReqDto.getResourceFile(), String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()), "resource_file");
            resourceFileEntity.setUrl(fileUrl);
        }

        return dtoUtil.prepRes(resourceFileEntity);
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public void delete(Long id) {
        LOG.info("----- Deleting Resource File. -----");

        Optional<ResourceFileEntity> optionalResourceFileEntity = resourceFileRepo.findById(id);
        optionalResourceFileEntity.orElseThrow(() -> new CustomException("RES002"));

        String fileUrl = optionalResourceFileEntity.get().getUrl();

        resourceFileRepo.delete(optionalResourceFileEntity.get());

        FileUtil.deleteFile(fileUrl);
    }
}
