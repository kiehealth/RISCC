package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByTitle(String title);

    List<GroupEntity> findAllByUsersIn(List<UserEntity> userEntities);

    @Modifying
    @Query(value = "DELETE FROM tbl_user_group WHERE user_id = :userId", nativeQuery = true)
    void deleteUserGroupByUserId(@Param("userId") Long userId);
}
