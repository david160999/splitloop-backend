package com.example.SplitLoop.group.infrastructure.config;

import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupConfig {

    @Bean
    public GroupService groupService(GroupMemberRepository groupMemberRepository) {
        return new GroupService(groupMemberRepository);
    }
}
