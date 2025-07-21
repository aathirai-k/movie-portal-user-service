package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDaoRepository;
import com.movie.portal.user_service.auth.Dto.LoginRequestDto;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.InvalidLoginException;
import com.movie.portal.user_service.auth.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserDaoRepository userDaoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserAuthServiceImpl authService;

    private User stubUser;

    @BeforeEach
    void setUp() {
        stubUser = new User();
        stubUser.setId(5L);
        stubUser.setUsername("test1");
        stubUser.setEmail("test7@gmail.com");
        stubUser.setPassword("$2a$10$hashed");  // value irrelevant—will be mocked
        stubUser.setRole("Admin");
    }

    @Nested
    @DisplayName("loginUser – happy path")
    class LoginSuccess {

        @Test
        void returnsLoginResponse_whenCredentialsAreValid() {
            // given
            LoginRequestDto req = new LoginRequestDto("test7@gmail.com", "plainPwd");
            given(userDaoRepository.findUserByEmail("test7@gmail.com")).willReturn(Optional.of(stubUser));
            given(passwordEncoder.matches("plainPwd", stubUser.getPassword())).willReturn(true);
            given(jwtService.generateToken(stubUser)).willReturn("dummy-token");

            // when
            var result = authService.loginUser(req);

            // then
            assertThat(result.getToken()).isEqualTo("dummy-token");
            assertThat(result.getUser().getId()).isEqualTo(5L);
            assertThat(result.getUser().getUserName()).isEqualTo("test1");
            assertThat(result.getUser().getUserRole()).isEqualTo("Admin");
        }
    }

    @Nested
    @DisplayName("loginUser – failure paths")
    class LoginFailures {

        @Test
        void throws_UserNotFoundException_whenEmailNotInDatabase() {
            // given
            LoginRequestDto req = new LoginRequestDto("absent@mail.com", "pwd");
            given(userDaoRepository.findUserByEmail(anyString())).willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> authService.loginUser(req))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void throws_InvalidLoginException_whenPasswordMismatch() {
            // given
            LoginRequestDto req = new LoginRequestDto("test7@gmail.com", "wrongPwd");
            given(userDaoRepository.findUserByEmail("test7@gmail.com")).willReturn(Optional.of(stubUser));
            given(passwordEncoder.matches("wrongPwd", stubUser.getPassword())).willReturn(false);

            // when / then
            assertThatThrownBy(() -> authService.loginUser(req))
                    .isInstanceOf(InvalidLoginException.class);
        }

        @Test
        void throws_IllegalArgumentException_whenEmailIsBlank() {
            LoginRequestDto req = new LoginRequestDto(" ", "pwd");
            assertThatThrownBy(() -> authService.loginUser(req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Email is required");
        }

        @Test
        void throws_IllegalArgumentException_whenPasswordIsBlank() {
            LoginRequestDto req = new LoginRequestDto("test@mail.com", "  ");
            assertThatThrownBy(() -> authService.loginUser(req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Password is required");
        }
    }
}
