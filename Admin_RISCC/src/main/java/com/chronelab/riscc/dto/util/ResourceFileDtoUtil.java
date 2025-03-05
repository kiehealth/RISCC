package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.ResourceFileReq;
import com.chronelab.riscc.dto.response.ResourceFileRes;
import com.chronelab.riscc.entity.ResourceFileEntity;
import org.springframework.stereotype.Component;

@Component
public class ResourceFileDtoUtil implements DtoUtil<ResourceFileEntity, ResourceFileReq, ResourceFileRes> {
    @Override
    public ResourceFileEntity reqToEntity(ResourceFileReq resourceFileReq) {
        return new ResourceFileEntity()
                .setTitle(resourceFileReq.getTitle())
                .setDescription(resourceFileReq.getDescription())
                .setUrl(resourceFileReq.getUrl());
    }

    @Override
    public ResourceFileRes entityToRes(ResourceFileEntity resourceFileEntity) {
        return new ResourceFileRes()
                .setId(resourceFileEntity.getId())
                .setTitle(resourceFileEntity.getTitle())
                .setDescription(resourceFileEntity.getDescription())
                .setUrl(resourceFileEntity.getUrl());
    }

    @Override
    public ResourceFileRes prepRes(ResourceFileEntity resourceFileEntity) {
        return entityToRes(resourceFileEntity);
    }

    @Override
    public void setUpdatedValue(ResourceFileReq resourceFileReq, ResourceFileEntity resourceFileEntity) {
        if (resourceFileReq != null && resourceFileEntity != null) {
            if (resourceFileReq.getTitle() != null && !resourceFileReq.getTitle().equals(resourceFileEntity.getTitle())) {
                resourceFileEntity.setTitle(resourceFileReq.getTitle());
            }
            if (resourceFileReq.getDescription() != null &&
                    (resourceFileEntity.getDescription() == null || !resourceFileReq.getDescription().equals(resourceFileEntity.getDescription()))) {
                resourceFileEntity.setDescription(resourceFileReq.getDescription());
            }
        }
    }
}
