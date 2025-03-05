package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.CountryReqDto;
import com.chronelab.riscc.dto.response.general.CountryResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CountryEntity;
import com.chronelab.riscc.enums.ErrorMessage;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.general.CountryRepo;
import com.chronelab.riscc.service.general.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountryServiceImpl implements CountryService {

    private static final Logger LOG = LogManager.getLogger();

    private final CountryRepo countryRepo;
    private final DtoUtil<CountryEntity, CountryReqDto, CountryResDto> dtoUtil;
    private final PaginationDtoUtil<CountryEntity, CountryReqDto, CountryResDto> paginationDtoUtil;

    @Autowired
    public CountryServiceImpl(CountryRepo countryRepo, DtoUtil<CountryEntity, CountryReqDto, CountryResDto> dtoUtil, PaginationDtoUtil<CountryEntity, CountryReqDto, CountryResDto> paginationDtoUtil) {
        this.countryRepo = countryRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('COUNTRY_C')")
    @Override
    public CountryResDto save(CountryReqDto countryReqDto) {
        LOG.info("----- Saving Country. -----");

        countryRepo.findByName(countryReqDto.getName()).ifPresent((countryEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.COU002.getMessage());
        }));

        return dtoUtil.prepRes(countryRepo.save(dtoUtil.reqToEntity(countryReqDto)));
    }

    @Override
    public PaginationResDto<CountryResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Country list. -----");

        List<String> fields = Arrays.asList("name", "dialCode", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, countryRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('COUNTRY_U')")
    @Override
    public CountryResDto update(CountryReqDto countryReqDto) {
        LOG.info("----- Updating Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(countryReqDto.getId());
        optionalCountryEntity.orElseThrow(() -> new CustomException("COU001"));
        CountryEntity countryEntity = optionalCountryEntity.get();

        dtoUtil.setUpdatedValue(countryReqDto, countryEntity);
        return dtoUtil.prepRes(countryEntity);
    }

    @PreAuthorize("hasAuthority('COUNTRY_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(id);
        optionalCountryEntity.orElseThrow(() -> new CustomException("COU001"));

        countryRepo.delete(optionalCountryEntity.get());
    }
}
