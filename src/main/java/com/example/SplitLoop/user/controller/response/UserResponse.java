package com.example.SplitLoop.user.controller.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email
){

}
