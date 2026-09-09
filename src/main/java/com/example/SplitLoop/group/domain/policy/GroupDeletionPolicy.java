package com.example.SplitLoop.group.domain.policy;

import com.example.SplitLoop.group.domain.entity.Group;

public interface GroupDeletionPolicy {

    void validateCanDelete(Group group);
}
