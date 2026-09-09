package com.example.SplitLoop.group.domain.repository;

import com.example.SplitLoop.group.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findByCreatedBy(UUID createdBy);

    @Query("""
        SELECT g
        FROM Group g
        JOIN GroupMember gm ON gm.group = g
        WHERE gm.user.id = :userId
        """)
    List<Group> findAllByUserId(@Param("userId") UUID userId);
}