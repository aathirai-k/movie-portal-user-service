package com.movie.portal.user_service.auth.Dto;

/**
 * Data Transfer Object for login information.
 * Used to transfer user data between client and server layers.
 */
public class LoginRequestDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
