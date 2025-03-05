package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.StateReqDto;
import com.chronelab.riscc.dto.response.general.StateResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CountryEntity;
import com.chronelab.riscc.entity.general.StateEntity;
import com.chronelab.riscc.enums.ErrorMessage;
import com.chronelab.riscc.repo.general.CountryRepo;
import com.chronelab.riscc.repo.general.StateRepo;
import com.chronelab.riscc.service.general.StateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StateServiceImpl implements StateService {

    private static final Logger LOG = LogManager.getLogger();

    private final StateRepo stateRepo;
    private final CountryRepo countryRepo;
    private final DtoUtil<StateEntity, StateReqDto, StateResDto> dtoUtil;
    private final PaginationDtoUtil<StateEntity, StateReqDto, StateResDto> paginationDtoUtil;

    @Autowired
    public StateServiceImpl(StateRepo stateRepo, CountryRepo countryRepo, DtoUtil<StateEntity, StateReqDto, StateResDto> dtoUtil, PaginationDtoUtil<StateEntity, StateReqDto, StateResDto> paginationDtoUtil) {
        this.stateRepo = stateRepo;
        this.countryRepo = countryRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('STATE_C')")
    @Override
    public StateResDto save(StateReqDto stateReqDto) {
        LOG.info("----- Saving State. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(stateReqDto.getCountryId());
        optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));

        stateRepo.findByNameAndCountry(stateReqDto.getName(), optionalCountryEntity.get()).ifPresent((stateEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.STA001.getMessage());
        }));

        StateEntity state = dtoUtil.reqToEntity(stateReqDto);
        state.setCountry(optionalCountryEntity.get());

        return dtoUtil.prepRes(stateRepo.save(state));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<StateResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated State list. -----");

        List<String> fields = Arrays.asList("name", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, stateRepo, dtoUtil);
    }

    @Override
    public PaginationResDto<StateResDto> getByCountry(Long countryId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting State by Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(countryId);
        optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));

        List<String> fields = Arrays.asList("name", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<StateEntity> stateEntityPage = null;
        List<StateEntity> stateEntities;
        if (paginationReqDto.getPageSize() > 0) {
            stateEntityPage = stateRepo.findAllByCountry(optionalCountryEntity.get(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            stateEntities = stateEntityPage.getContent();
        } else {
            stateEntities = stateRepo.findAllByCountry(optionalCountryEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<StateResDto> stateResDtos = stateEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(stateEntityPage, stateResDtos);
    }

    @PreAuthorize("hasAuthority('STATE_U')")
    @Override
    public StateResDto update(StateReqDto stateReqDto) {
        LOG.info("----- Updating State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(stateReqDto.getId());
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        if (stateReqDto.getCountryId() != null) {
            Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(stateReqDto.getCountryId());
            optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));
            optionalStateEntity.get().setCountry(optionalCountryEntity.get());
        }

        dtoUtil.setUpdatedValue(stateReqDto, optionalStateEntity.get());
        return dtoUtil.prepRes(optionalStateEntity.get());
    }

    @PreAuthorize("hasAuthority('STATE_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(id);
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        stateRepo.delete(optionalStateEntity.get());
    }
}
