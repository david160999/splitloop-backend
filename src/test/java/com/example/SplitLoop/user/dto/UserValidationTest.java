package com.example.SplitLoop.user.dto;


import com.example.SplitLoop.user.application.command.ChangePasswordUseCase;
import com.example.SplitLoop.user.application.query.SearchUsersUseCase;
import com.example.SplitLoop.user.application.command.UpdateCurrentUserUseCase;
import com.example.SplitLoop.user.infrastructure.presentation.rest.controller.UserController;
import com.example.SplitLoop.user.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

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
