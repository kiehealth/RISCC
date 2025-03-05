package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.NoteEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<NoteEntity, Long> {
    Page<NoteEntity> findAllByUser_Id(Long userId, Pageable pageable);

    List<NoteEntity> findAllByUser_Id(Long userId, Sort sort);

    void deleteAllByUser(UserEntity userEntity);
}
