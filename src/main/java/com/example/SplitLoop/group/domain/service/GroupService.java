package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.User;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    //Group Service//
    Group createGroup(User creator, String name);

    void updateGroup(Group group, String name, String description) throws BadRequestException;

    void deleteGroup(Group group, User currentUser);

    void removeMember(Group group, User requester, User memberToRemove);

    void leaveGroup(Group group, User user);

    void validateGroupDeletion(Group group);

    List<Group> getGroupsByUserId(UUID userId);

    //GroupMember Service//
    GroupMember addMember(Group group, User user, MemberRole role);

    GroupMember updateMemberRole(Group group, User requestedBy, User targetUser, MemberRole newRole);

    GroupMember getMember(Group group, User user);

    void validateLastAdmin(Group group, GroupMember member);

    boolean isMember(UUID groupId, UUID userId);

    long countAdmins(UUID groupId);

    boolean isCreator(Group group, UUID userId);

    void validateMember(UUID groupId, UUID userId);

    List<GroupMember> getMembers(Group group);

    GroupMember getCurrentMember(Group group);
}