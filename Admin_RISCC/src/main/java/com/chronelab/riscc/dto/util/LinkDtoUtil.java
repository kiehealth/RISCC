package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.LinkReq;
import com.chronelab.riscc.dto.response.LinkRes;
import com.chronelab.riscc.entity.LinkEntity;
import org.springframework.stereotype.Component;

@Component
public class LinkDtoUtil implements DtoUtil<LinkEntity, LinkReq, LinkRes> {
    @Override
    public LinkEntity reqToEntity(LinkReq linkReq) {
        return new LinkEntity()
                .setTitle(linkReq.getTitle())
                .setDescription(linkReq.getDescription())
                .setUrl(linkReq.getUrl())
                .setEmailAddress(linkReq.getEmailAddress())
                .setContactNumber(linkReq.getContactNumber());
    }

    @Override
    public LinkRes entityToRes(LinkEntity linkEntity) {
        return new LinkRes()
                .setId(linkEntity.getId())
                .setTitle(linkEntity.getTitle())
                .setDescription(linkEntity.getDescription())
                .setUrl(linkEntity.getUrl())
                .setEmailAddress(linkEntity.getEmailAddress())
                .setContactNumber(linkEntity.getContactNumber());
    }

    @Override
    public LinkRes prepRes(LinkEntity linkEntity) {
        return entityToRes(linkEntity);
    }

    @Override
    public void setUpdatedValue(LinkReq linkReq, LinkEntity linkEntity) {
        if (linkReq != null && linkEntity != null) {
            if (linkReq.getTitle() != null && !linkReq.getTitle().equals(linkEntity.getTitle())) {
                linkEntity.setTitle(linkReq.getTitle());
            }
            if (linkReq.getDescription() != null
                    && (linkEntity.getDescription() == null || !linkReq.getDescription().equals(linkEntity.getDescription()))) {
                linkEntity.setDescription(linkReq.getDescription());
            }
            if (linkReq.getEmailAddress() != null
                    && (linkEntity.getEmailAddress() == null || !linkReq.getEmailAddress().equals(linkEntity.getEmailAddress()))) {
                linkEntity.setEmailAddress(linkReq.getEmailAddress());
            }
            if (linkReq.getContactNumber() != null
                    && (linkEntity.getContactNumber() == null || !linkReq.getContactNumber().equals(linkEntity.getContactNumber()))) {
                linkEntity.setContactNumber(linkReq.getContactNumber());
            }
            if (linkReq.getUrl() != null
                    && (linkEntity.getUrl() == null || !linkReq.getUrl().equals(linkEntity.getUrl()))) {
                linkEntity.setUrl(linkReq.getUrl());
            }
        }
    }
}
