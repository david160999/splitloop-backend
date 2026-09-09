package com.example.SplitLoop.group.controller;

import com.example.SplitLoop.group.application.query.GetGroupMembersUseCase;
import com.example.SplitLoop.group.application.query.GetGroupSummaryUseCase;
import com.example.SplitLoop.group.application.query.GetGroupUseCase;
import com.example.SplitLoop.group.application.query.GetGroupsByUserUseCase;
import com.example.SplitLoop.group.application.usecase.*;
import com.example.SplitLoop.group.controller.query.GetGroupMembersQuery;
import com.example.SplitLoop.group.controller.query.GetGroupsByUserQuery;
import com.example.SplitLoop.group.controller.request.*;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.controller.response.GroupSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Groups")
public class GroupController {

    private final AddMemberUseCase addMemberUseCase;
    private final CreateGroupUseCase createGroupUseCase;
    private final DeleteGroupUseCase deleteGroupUseCase;
    private final GetGroupMembersUseCase getGroupMembersUseCase;
    private final GetGroupSummaryUseCase getGroupSummaryUseCase;
    private final GetGroupUseCase getGroupUseCase;
    private final GetGroupsByUserUseCase getGroupsByUserUseCase;
    private final LeaveGroupUseCase leaveGroupUseCase;
    private final RemoveMemberUseCase removeMemberUseCase;
    private final UpdateGroupUseCase updateGroupUseCase;
    private final UpdateMemberRoleUseCase updateMemberRoleUseCase;

    @PostMapping
    @Operation(summary = "Create a new group")
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {

        GroupResponse response = createGroupUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get user groups")
    public ResponseEntity<List<GroupResponse>> getGroups(@Valid GetGroupsByUserQuery query) {

        return ResponseEntity.ok(getGroupsByUserUseCase.execute(query));
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Get group")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable UUID groupId) {

        return ResponseEntity.ok(getGroupUseCase.execute(groupId));
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "Update group")
    public ResponseEntity<GroupResponse> updateGroup(
            @PathVariable UUID groupId,
            @Valid @RequestBody UpdateGroupRequest request) throws BadRequestException {

        return ResponseEntity.ok(updateGroupUseCase.execute(groupId, request));
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "Delete group")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {

        deleteGroupUseCase.execute(groupId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/members")
    @Operation(summary = "Add member")
    public ResponseEntity<GroupMemberResponse> addMember(
            @PathVariable UUID groupId,
            @Valid @RequestBody AddMemberRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addMemberUseCase.execute(groupId, request));
    }

    @GetMapping("/{groupId}/members")
    @Operation(summary = "Get group members")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(@Valid GetGroupMembersQuery query) {

        return ResponseEntity.ok(getGroupMembersUseCase.execute(query));
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    @Operation(summary = "Remove member")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID groupId,
            @PathVariable UUID memberId) {

        removeMemberUseCase.execute(groupId, memberId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/leave")
    @Operation(summary = "Leave group")
    public ResponseEntity<Void> leaveGroup(@PathVariable UUID groupId) {

        leaveGroupUseCase.execute(groupId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{groupId}/members/{memberId}/role")
    @Operation(summary = "Update member role")
    public ResponseEntity<GroupMemberResponse> updateMemberRole(
            @PathVariable UUID groupId,
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request) {

        return ResponseEntity.ok(updateMemberRoleUseCase.execute(groupId, memberId, request));
    }

    @GetMapping("/{groupId}/summary")
    @Operation(summary = "Get group summary")
    public ResponseEntity<GroupSummaryResponse> getSummary(@PathVariable UUID groupId) {

        return ResponseEntity.ok(getGroupSummaryUseCase.execute(groupId));
    }
}