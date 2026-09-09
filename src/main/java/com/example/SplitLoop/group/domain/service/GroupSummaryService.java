package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.modelo.GroupSummary;

public interface GroupSummaryService {

    GroupSummary getSummary(Group group);
}