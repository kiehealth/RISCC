package com.chronelab.riscc.repo.general;

import com.chronelab.riscc.entity.general.CountryEntity;
import com.chronelab.riscc.entity.general.StateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StateRepo extends JpaRepository<StateEntity, Long> {
    Optional<StateEntity> findByNameAndCountry(String name, CountryEntity countryEntity);

    Page<StateEntity> findAllByCountry(CountryEntity countryEntity, Pageable pageable);

    List<StateEntity> findAllByCountry(CountryEntity countryEntity, Sort sort);
}
