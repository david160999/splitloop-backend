package com.example.SplitLoop.group.infrastructure.persistence.jpa;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataGroupRepository extends JpaRepository<GroupEntity, UUID> {

    List<GroupEntity> findByCreatedById(UUID createdById);

    @Query("""
        SELECT g
        FROM GroupEntity g
        JOIN GroupMemberEntity gm ON gm.group = g
        WHERE gm.user.id = :userId
        """)
    List<GroupEntity> findAllByUserId(@Param("userId") UUID userId);


}
