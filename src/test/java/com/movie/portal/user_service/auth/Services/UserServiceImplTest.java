package com.movie.portal.user_service.auth.Services;

import com.movie.portal.user_service.auth.Dao.UserDao;
import com.movie.portal.user_service.auth.Dto.UserDto;
import com.movie.portal.user_service.auth.Dto.UserResponse;
import com.movie.portal.user_service.auth.Model.User;
import com.movie.portal.user_service.auth.exception.DatabaseException;
import com.movie.portal.user_service.auth.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserDao userDao;
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void registerUser_success() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setUserName("testuser");
        dto.setPassword("password123");
        dto.setUserRole("USER");

        when(userDao.findUserByEmail(dto.getEmail())).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(dto.getEmail());
        savedUser.setUsername(dto.getUserName());
        savedUser.setRole(dto.getUserRole());
        savedUser.setPassword("encodedPassword");

        when(userDao.saveUser(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(dto.getUserName(), response.getUserName());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getUserRole(), response.getUserRole());

        verify(userDao).findUserByEmail(dto.getEmail());
        verify(userDao).saveUser(any(User.class));
    }

    @Test
    void registerUser_duplicateEmail_throwsException() {
        UserDto dto = new UserDto();
        dto.setEmail("existing@example.com");

        when(userDao.findUserByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(dto));

        verify(userDao).findUserByEmail(dto.getEmail());
        verify(userDao, never()).saveUser(any());
    }

    @Test
    void registerUser_databaseError_throwsDatabaseException() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setUserName("testuser");
        dto.setPassword("password123");
        dto.setUserRole("USER");

        when(userDao.findUserByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userDao.saveUser(any(User.class))).thenThrow(new DataAccessResourceFailureException("DB error"));

        assertThrows(DatabaseException.class, () -> userService.registerUser(dto));

        verify(userDao).findUserByEmail(dto.getEmail());
        verify(userDao).saveUser(any(User.class));
    }
}
