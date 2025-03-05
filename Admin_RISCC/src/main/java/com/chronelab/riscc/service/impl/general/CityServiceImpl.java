package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.CityReqDto;
import com.chronelab.riscc.dto.response.general.CityResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CityEntity;
import com.chronelab.riscc.entity.general.StateEntity;
import com.chronelab.riscc.enums.ErrorMessage;
import com.chronelab.riscc.repo.general.CityRepo;
import com.chronelab.riscc.repo.general.StateRepo;
import com.chronelab.riscc.service.general.CityService;
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
public class CityServiceImpl implements CityService {

    private static final Logger LOG = LogManager.getLogger();

    private final CityRepo cityRepo;
    private final StateRepo stateRepo;
    private final DtoUtil<CityEntity, CityReqDto, CityResDto> dtoUtil;
    private final PaginationDtoUtil<CityEntity, CityReqDto, CityResDto> paginationDtoUtil;

    @Autowired
    public CityServiceImpl(CityRepo cityRepo, StateRepo stateRepo, DtoUtil<CityEntity, CityReqDto, CityResDto> dtoUtil, PaginationDtoUtil<CityEntity, CityReqDto, CityResDto> paginationDtoUtil) {
        this.cityRepo = cityRepo;
        this.stateRepo = stateRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('CITY_C')")
    @Override
    public CityResDto save(CityReqDto cityReqDto) {
        LOG.info("----- Saving City. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(cityReqDto.getStateId());
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        cityRepo.findByNameAndState(cityReqDto.getName(), optionalStateEntity.get()).ifPresent((stateEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.CIT001.getMessage());
        }));

        CityEntity city = dtoUtil.reqToEntity(cityReqDto);
        city.setState(optionalStateEntity.get());

        return dtoUtil.prepRes(cityRepo.save(city));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<CityResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated City list. -----");

        List<String> fields = Arrays.asList("name");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, cityRepo, dtoUtil);
    }

    @Override
    public PaginationResDto<CityResDto> getByState(Long stateId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting City by State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(stateId);
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        List<String> fields = Arrays.asList("name");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<CityEntity> cityEntityPage = null;
        List<CityEntity> cityEntities;
        if (paginationReqDto.getPageSize() > 0) {
            cityEntityPage = cityRepo.findAllByState(optionalStateEntity.get(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            cityEntities = cityEntityPage.getContent();
        } else {
            cityEntities = cityRepo.findAllByState(optionalStateEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<CityResDto> cityResDtos = cityEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(cityEntityPage, cityResDtos);
    }

    @PreAuthorize("hasAuthority('CITY_U')")
    @Override
    public CityResDto update(CityReqDto cityReqDto) {
        LOG.info("----- Updating City. -----");

        Optional<CityEntity> optionalCityEntity = cityRepo.findById(cityReqDto.getId());
        optionalCityEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CIT002.getMessage()));

        if (cityReqDto.getStateId() != null) {
            Optional<StateEntity> optionalStateEntity = stateRepo.findById(cityReqDto.getStateId());
            optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));
            optionalCityEntity.get().setState(optionalStateEntity.get());
        }

        dtoUtil.setUpdatedValue(cityReqDto, optionalCityEntity.get());
        return dtoUtil.prepRes(optionalCityEntity.get());
    }

    @PreAuthorize("hasAuthority('CITY_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting City. -----");

        Optional<CityEntity> optionalCityEntity = cityRepo.findById(id);
        optionalCityEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CIT002.getMessage()));

        if (optionalCityEntity.get().getAddresses() != null && optionalCityEntity.get().getAddresses().size() > 0) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ErrorMessage.CIT003.getMessage());
        }

        cityRepo.delete(optionalCityEntity.get());
    }
}
