package com.movie.portal.user_service.auth.Dto;

public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private String userRole;

    public UserResponse(Long id, String userName, String email, String userRole) {
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
