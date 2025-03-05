package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.NoteReq;
import com.chronelab.riscc.dto.response.NoteRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.NoteDtoUtil;
import com.chronelab.riscc.entity.NoteEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.NoteRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('NOTE')")
@Service
@Transactional(rollbackFor = Exception.class)
public class NoteService {

    private static final Logger LOG = LogManager.getLogger();

    private final NoteRepo noteRepo;
    private final UserRepo userRepo;
    private final DtoUtil<NoteEntity, NoteReq, NoteRes> dtoUtil;
    private final PaginationDtoUtil<NoteEntity, NoteReq, NoteRes> paginationDtoUtil;

    @Autowired
    public NoteService(NoteRepo noteRepo, UserRepo userRepo, NoteDtoUtil noteDtoUtil, PaginationDtoUtil<NoteEntity, NoteReq, NoteRes> paginationDtoUtil) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.dtoUtil = noteDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('NOTE_C')")
    public NoteRes save(NoteReq noteReq) {
        LOG.info("----- Saving Note. -----");
        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        NoteEntity note = dtoUtil.reqToEntity(noteReq).setUser(optionalUserEntity.get());
        return dtoUtil.prepRes(noteRepo.save(note));
    }

    public PaginationResDto<NoteRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Note. -----");

        List<String> fields = Arrays.asList("title", "description", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<NoteEntity> noteEntityPage = null;
        List<NoteEntity> noteEntities;
        if (paginationReqDto.getPageSize() > 0) {
            noteEntityPage = noteRepo.findAllByUser_Id(SessionManager.getUserId(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            noteEntities = noteEntityPage.getContent();
        } else {
            noteEntities = noteRepo.findAllByUser_Id(SessionManager.getUserId(), Sort.by(sortOrder, sortBy));
        }

        List<NoteRes> noteResDtos = noteEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(noteEntityPage, noteResDtos);
    }

    @PreAuthorize("hasAuthority('NOTE_U')")
    public NoteRes update(NoteReq noteReq) {
        LOG.info("----- Updating Note. -----");

        Optional<NoteEntity> NoteEntity = noteRepo.findById(noteReq.getId());
        NoteEntity.orElseThrow(() -> new CustomException("NOT001"));

        dtoUtil.setUpdatedValue(noteReq, NoteEntity.get());

        return dtoUtil.prepRes(NoteEntity.get());
    }

    @PreAuthorize("hasAuthority('NOTE_D')")
    public void delete(Long id) {
        LOG.info("----- Deleting Note. -----");

        Optional<NoteEntity> NoteEntity = noteRepo.findById(id);
        NoteEntity.orElseThrow(() -> new CustomException("NOT001"));

        noteRepo.delete(NoteEntity.get());
    }
}
