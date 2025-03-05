package com.chronelab.riscc.repo.general;

import com.chronelab.riscc.entity.general.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<AuthorityEntity, Long> {
}
