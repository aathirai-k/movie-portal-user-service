package com.movie.portal.user_service.auth.Dto;

/**
 * Data Transfer Object for User information.
 * Used to transfer user data between client and server layers.
 */
public class UserDto     {

    private String userName;
    private String email;
    private String password;
    private String userRole;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
