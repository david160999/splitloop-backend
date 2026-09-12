package com.example.SplitLoop.user.dto;


import com.example.SplitLoop.user.application.command.ChangePasswordUseCase;
import com.example.SplitLoop.user.application.command.SearchUsersUseCase;
import com.example.SplitLoop.user.application.command.UpdateCurrentUserUseCase;
import com.example.SplitLoop.user.controller.UserController;
import com.example.SplitLoop.user.domain.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser
@ActiveProfiles("test")
class UserValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SearchUsersUseCase searchUsersUseCase;

    @MockitoBean
    private UpdateCurrentUserUseCase updateCurrentUserUseCase;

    @MockitoBean
    private ChangePasswordUseCase changePasswordUseCase;


}
