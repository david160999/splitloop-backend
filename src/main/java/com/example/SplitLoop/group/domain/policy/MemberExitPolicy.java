package com.example.SplitLoop.group.domain.policy;

import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;

public interface MemberExitPolicy {

    void validateCanExist(Group group, User user);

}