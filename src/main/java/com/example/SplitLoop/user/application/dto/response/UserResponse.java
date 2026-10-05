package com.example.SplitLoop.user.application.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email
){

}
