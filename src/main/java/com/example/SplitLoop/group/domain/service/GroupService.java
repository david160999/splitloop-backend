package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    //Group Service//
    Group createGroup(UserEntity creator, String name);

    void updateGroup(Group group, String name, String description);

    void deleteGroup(Group group, UserEntity currentUserEntity);

    void removeMember(Group group, UserEntity requester, UserEntity memberToRemove);

    void leaveGroup(Group group, UserEntity userEntity);

    void validateGroupDeletion(Group group);

    List<Group> getGroupsByUserId(UUID userId);

    //GroupMember Service//
    GroupMember addMember(Group group, UserEntity userEntity, MemberRole role);

    GroupMember updateMemberRole(Group group, UserEntity requestedBy, UserEntity targetUserEntity, MemberRole newRole);

    GroupMember getMember(Group group, UserEntity userEntity);

    void validateLastAdmin(Group group, GroupMember member);

    boolean isMember(UUID groupId, UUID userId);

    long countAdmins(UUID groupId);

    boolean isCreator(Group group, UUID userId);

    void validateMember(UUID groupId, UUID userId);

    List<GroupMember> getMembers(Group group);

    GroupMember getCurrentMember(Group group);
}