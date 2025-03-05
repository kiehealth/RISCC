package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.RisccRangeAndMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface RisccRangeRepo extends JpaRepository<RisccRangeAndMessageEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT message from tbl_riscc_range WHERE :value >= CAST(`from_value` AS DOUBLE) AND :value < CAST(`to_value` as DOUBLE)")
    String findMessageByRisccValue(@Param("value") BigDecimal risccValue);

    @Query(nativeQuery = true, value = "SELECT more_info from tbl_riscc_range WHERE :value >= CAST(`from_value` AS DOUBLE) AND :value < CAST(`to_value` as DOUBLE)")
    String findMoreInfoByRisccValue(@Param("value") BigDecimal risccValue);
}