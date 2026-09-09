package com.example.SplitLoop.user.service;

import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.service.UserServiceImpl;
import com.example.SplitLoop.user.mapper.UserMapper;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WithMockUser
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    private User createUser() {

        return User.builder()
                .id(UUID.randomUUID())
                .username("john")
                .email("john@test.com")
                .password("1234")
                .build();
    }


//    @Test
//    void shouldCreateUserSuccessfully() {
//
//        // given
//        CreateUserRequest dto = CreateUserRequest.builder().build();
//
//        User user = User.builder().build();
//
//        User saved = User.builder()
//                .id(UUID.randomUUID())
//                .build();
//
//        UserResponse response = UserResponse.builder().id(saved.getId()).build();
//
//        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
//        when(userMapper.toEntity(dto)).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(saved);
//        when(userMapper.toResponse(saved)).thenReturn(response);
//
//        // when
//        UserResponse result = userService.createUser(dto);
//
//        // then
//        assertEquals("John", result.name());
//        assertEquals("john@test.com", result.email());
//    }
//
//    @Test
//    void shouldReturnUserById() {
//
//        UUID id = UUID.randomUUID();
//
//        User user = User.builder()
//                .id(id)
//                .build();
//
//        UserResponse response = UserResponse.builder()
//                .id(id)
//                .build();
//
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        when(userMapper.toResponse(user)).thenReturn(response);
//
//        UserResponse result = userService.getById(id);
//
//        assertEquals(id, result.id());
//    }
//
//    @Test
//    void shouldReturnUserByEmail() {
//
//        String email = "john@test.com";
//
//        User user = User.builder()
//                .id(UUID.randomUUID())
//                .build();
//
//        UserResponse response = UserResponse.builder()
//                .id(user.getId())
//                .build();
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//        when(userMapper.toResponse(user)).thenReturn(response);
//
//        UserResponse result = userService.getByEmail(email);
//
//        assertEquals(email, result.email());
//    }

}