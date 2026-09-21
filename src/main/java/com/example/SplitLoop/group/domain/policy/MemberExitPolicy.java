package com.example.SplitLoop.group.domain.policy;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.user.domain.entity.UserEntity;

public interface MemberExitPolicy {

    void validateCanExist(Group group, UserEntity userEntity);
}