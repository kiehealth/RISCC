package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.LinkReq;
import com.chronelab.riscc.dto.response.LinkRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.LinkDtoUtil;
import com.chronelab.riscc.entity.LinkEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.LinkRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAuthority('LINK')")
@Service
@Transactional(rollbackFor = Exception.class)
public class LinkService {

    private static final Logger LOG = LogManager.getLogger();

    private final LinkRepo linkRepo;
    private final DtoUtil<LinkEntity, LinkReq, LinkRes> dtoUtil;
    private final PaginationDtoUtil<LinkEntity, LinkReq, LinkRes> paginationDtoUtil;

    @Autowired
    public LinkService(LinkRepo linkRepo, LinkDtoUtil linkDtoUtil, PaginationDtoUtil<LinkEntity, LinkReq, LinkRes> paginationDtoUtil) {
        this.linkRepo = linkRepo;
        this.dtoUtil = linkDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('LINK_C')")
    public LinkRes save(LinkReq linkReq) {
        LOG.info("----- Saving Link. -----");

        if (linkRepo.findByTitle(linkReq.getTitle()).isPresent()) {
            throw new CustomException("LNK001");
        }

        return dtoUtil.prepRes(linkRepo.save(dtoUtil.reqToEntity(linkReq)));
    }

    public PaginationResDto<LinkRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Link. -----");

        List<String> fields = Arrays.asList("title", "description", "emailAddress", "contactNumber", "url");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, linkRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('LINK_U')")
    public LinkRes update(LinkReq linkReq) {
        LOG.info("----- Updating Link. -----");

        Optional<LinkEntity> LinkEntity = linkRepo.findById(linkReq.getId());
        LinkEntity.orElseThrow(() -> new CustomException("LKN002"));

        if (linkReq.getTitle() != null && !linkReq.getTitle().equalsIgnoreCase(LinkEntity.get().getTitle())
                && linkRepo.findByTitle(linkReq.getTitle()).isPresent()) {
            throw new CustomException("LNK001");
        }

        dtoUtil.setUpdatedValue(linkReq, LinkEntity.get());

        return dtoUtil.prepRes(LinkEntity.get());
    }

    @PreAuthorize("hasAuthority('LINK_D')")
    public void delete(Long id) {
        LOG.info("----- Deleting Link. -----");

        Optional<LinkEntity> optionalLinkEntity = linkRepo.findById(id);
        optionalLinkEntity.orElseThrow(() -> new CustomException("LNK002"));

        linkRepo.delete(optionalLinkEntity.get());
    }
}
