package com.chronelab.riscc.repo.general;

import com.chronelab.riscc.entity.general.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepo extends JpaRepository<CountryEntity, Long> {

    Optional<CountryEntity> findByName(String name);
}
