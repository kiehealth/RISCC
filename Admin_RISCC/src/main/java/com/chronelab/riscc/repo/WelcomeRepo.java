package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.WelcomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelcomeRepo extends JpaRepository<WelcomeEntity, Long> {
}
