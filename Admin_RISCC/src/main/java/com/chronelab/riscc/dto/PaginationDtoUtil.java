package com.chronelab.riscc.dto;

import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CommonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaginationDtoUtil<Entity extends CommonEntity, Req, Res> {

    public PaginationResDto<Res> prepPaginationDto(Page<Entity> page, List<Res> data) {
        PaginationResDto<Res> paginationResDto = new PaginationResDto<>();

        if (page != null) {
            paginationResDto
                    .setStartPosition(page.getNumber() * page.getSize() + 1)
                    .setEndPosition((page.getNumber() * page.getSize() + 1) + (page.getContent().size() - 1))
                    .setTotalRecord(page.getTotalElements())
                    .setTotalPage(page.getTotalPages())
                    .setPageSize(page.getSize())
                    .setCurrentPage(page.getNumber() + 1);
        }
        return paginationResDto.setData(data);
    }

    public PaginationResDto<Res> paginate(PaginationReqDto paginationReqDto, List<String> fields, String sortBy, Sort.Direction sortOrder, JpaRepository<Entity, Long> repository, DtoUtil<Entity, Req, Res> dtoUtil) {
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<Entity> entityPage = null;
        List<Entity> entities;
        if (paginationReqDto.getPageSize() > 0) {
            entityPage = repository.findAll(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            entities = entityPage.getContent();
        } else {
            entities = repository.findAll(Sort.by(sortOrder, sortBy));
        }

        List<Res> resDtos = entities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return prepPaginationDto(entityPage, resDtos);
    }
}
