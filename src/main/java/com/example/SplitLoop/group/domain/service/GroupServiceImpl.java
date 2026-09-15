package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.group.domain.policy.GroupDeletionPolicy;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.exception.*;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final CurrentUserService currentUserService;

    private final MemberExitPolicy memberExitPolicy;
    private final List<GroupDeletionPolicy> groupDeletionPolicies;

    @Override
    public Group createGroup(User creator, String name) {

        Group group = Group.builder()
                .name(name)
                .createdBy(creator)
                .createdAt(LocalDateTime.now(ZoneId.systemDefault()))
                .build();

        group = groupRepository.save(group);

        GroupMember admin = GroupMember.builder()
                .group(group)
                .user(creator)
                .memberRole(MemberRole.ADMIN)
                .build();

        memberRepository.save(admin);

        return group;
    }

    @Override
    public void updateGroup(Group group, String name, String description){
        group.setName(name);
        group.setDescription(description);
    }

    @Override
    public void deleteGroup(Group group, User requester) {

        if (!group.getCreatedBy().getId().equals(requester.getId())) {
            throw new OnlyGroupCreatorCanDeleteException();
        }

        groupDeletionPolicies.forEach(policy -> policy.validateCanDelete(group));

        groupRepository.delete(group);
    }

    @Override
    public void validateGroupDeletion(Group group) {
    }


    @Override
    public GroupMember addMember(Group group, User user, MemberRole memberRole) {

        if (isMember(group.getId(), user.getId())) {
            throw new UserAlreadyInGroupException(user.getId(), group.getId());
        }

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .memberRole(memberRole)
                .build();

        member = memberRepository.save(member);

        return member;
    }

    @Override
    public void removeMember(Group group, User requester, User memberToRemove) {

        GroupMember requesterMember = getMember(group, requester);

        if(isCreator(group, memberToRemove.getId())){
            throw new CannotRemoveGroupCreatorException();
        }

        if (!requesterMember.isAdmin()) {
            throw new InsufficientPermissionsException();
        }

        GroupMember member = getMember(group, memberToRemove);

        validateLastAdmin(group, member);

        memberExitPolicy.validateCanExist(group, memberToRemove);

        memberRepository.delete(member);
    }

    @Override
    public void leaveGroup(Group group, User user) {

        GroupMember member = getMember(group, user);

        validateLastAdmin(group, member);

        memberExitPolicy.validateCanExist(group, user);

        memberRepository.delete(member);
    }

    @Override
    public GroupMember updateMemberRole(Group group, User requestedBy, User targetUser, MemberRole newRole) {

        if(isCreator(group, targetUser.getId())){
            throw new CannotChangeGroupCreatorRoleException();
        }

        GroupMember requester = getMember(group, requestedBy);

        if (!requester.isAdmin()) {
            throw new InsufficientPermissionsException();
        }

        GroupMember target = getMember(group, targetUser);

        if (target.isAdmin() && newRole != MemberRole.ADMIN) {
            validateLastAdmin(group, target);
        }

        target.setMemberRole(newRole);
        return requester;
    }

    @Override
    public GroupMember getMember(Group group, User user) {

        return memberRepository
                .findByGroupAndUser(group, user)
                .orElseThrow(() -> new UserNotInGroupException(user.getId(), group.getId()));
    }

    @Override
    public void validateLastAdmin(Group group, GroupMember member) {

        if (member.isAdmin() && countAdmins(group.getId()) == 1) {
            throw new LastAdminInGroupException();
        }
    }

    @Override
    public boolean isMember(UUID groupId, UUID userId) {
        return memberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public long countAdmins(UUID groupId) {
        return memberRepository.countByGroupIdAndMemberRole(
                groupId,
                MemberRole.ADMIN
        );
    }

    @Override
    public boolean isCreator(Group group, UUID userId) {
        return group.getCreatedBy() != null && group.getCreatedBy().getId().equals(userId);
    }

    @Override
    public void validateMember(UUID groupId, UUID userId) {

        if (!isMember(groupId, userId)) {
            throw new UserNotInGroupException(userId, groupId);
        }
    }

    @Override
    public List<Group> getGroupsByUserId(UUID userId) {

        return groupRepository.findAllByUserId(userId);
    }

    @Override
    public List<GroupMember> getMembers(Group group) {
        return memberRepository.findAllByGroup(group);
    }

    @Override
    public GroupMember getCurrentMember(Group group) {
        User currentUser = currentUserService.getCurrentUser();

        return getMember(group, currentUser);
    }
}