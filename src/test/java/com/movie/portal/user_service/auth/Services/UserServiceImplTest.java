package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDaoRepository;
import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.DatabaseException;
import com.movie.portal.user_service.auth.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserDaoRepository userDaoRepository;
    private UserServiceImpl userService;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setup() {
        userDaoRepository = Mockito.mock(UserDaoRepository.class);
        encoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userDaoRepository, encoder);
    }

    @Test
    void registerUser_success() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setUserName("testuser");
        dto.setPassword("password123");
        dto.setUserRole("USER");

        when(userDaoRepository.findUserByEmail(dto.getEmail())).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(dto.getEmail());
        savedUser.setUsername(dto.getUserName());
        savedUser.setRole(dto.getUserRole());
        savedUser.setPassword("encodedPassword");

        when(userDaoRepository.saveUser(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(dto.getUserName(), response.getUserName());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getUserRole(), response.getUserRole());

        verify(userDaoRepository).findUserByEmail(dto.getEmail());
        verify(userDaoRepository).saveUser(any(User.class));
    }

    @Test
    void registerUser_duplicateEmail_throwsException() {
        UserDto dto = new UserDto();
        dto.setEmail("existing@example.com");

        when(userDaoRepository.findUserByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(dto));

        verify(userDaoRepository).findUserByEmail(dto.getEmail());
        verify(userDaoRepository, never()).saveUser(any());
    }

    @Test
    void registerUser_databaseError_throwsDatabaseException() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setUserName("testuser");
        dto.setPassword("password123");
        dto.setUserRole("USER");

        when(userDaoRepository.findUserByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userDaoRepository.saveUser(any(User.class))).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DatabaseException.class, () -> userService.registerUser(dto));

        verify(userDaoRepository).findUserByEmail(dto.getEmail());
        verify(userDaoRepository).saveUser(any(User.class));
    }

    @Test
    void testUpdateUser_AsAdmin_UpdatesAllFields() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setRole("USER");
        existingUser.setUsername("oldUser");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPass");

        UserDto updateRequest = new UserDto();
        updateRequest.setUserRole("ADMIN");
        updateRequest.setUserName("newUser");
        updateRequest.setEmail("new@example.com");
        updateRequest.setPassword("newPass");

        when(userDaoRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(encoder.encode("newPass")).thenReturn("encodedPass");

        userService.updateUser(userId, updateRequest, true);

        assertEquals("ADMIN", existingUser.getRole());
        assertEquals("newUser", existingUser.getUsername());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("encodedPass", existingUser.getPassword());

        verify(userDaoRepository).updateUser(existingUser);
    }

    @Test
    void testUpdateUser_AsAdmin_UpdatesRoleField() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setRole("USER");

        UserDto updateRequest = new UserDto();
        updateRequest.setUserRole("ADMIN");

        when(userDaoRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateUser(userId, updateRequest, true);

        assertEquals("ADMIN", existingUser.getRole());

        verify(userDaoRepository).updateUser(existingUser);
    }

    @Test
    void testUpdateUser_AsAdmin_UpdatesEmailField() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        UserDto updateRequest = new UserDto();
        updateRequest.setEmail("new@example.com");

        when(userDaoRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateUser(userId, updateRequest, true);

        assertEquals("new@example.com", existingUser.getEmail());

        verify(userDaoRepository).updateUser(existingUser);
    }

    @Test
    void testUpdateUser_AsAdmin_UpdatesUsernameField() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUser");

        UserDto updateRequest = new UserDto();
        updateRequest.setUserName("newUser");

        when(userDaoRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateUser(userId, updateRequest, true);

        assertEquals("newUser", existingUser.getUsername());

        verify(userDaoRepository).updateUser(existingUser);
    }

    @Test
    void testUpdateUser_AsAdmin_UpdatesPasswordField() {
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setPassword("oldPass");

        UserDto updateRequest = new UserDto();
        updateRequest.setPassword("newPass");

        when(userDaoRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(encoder.encode("newPass")).thenReturn("encodedPass");

        userService.updateUser(userId, updateRequest, true);

        assertEquals("encodedPass", existingUser.getPassword());

        verify(userDaoRepository).updateUser(existingUser);
    }

}
