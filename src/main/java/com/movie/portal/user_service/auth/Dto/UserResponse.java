package com.movie.portal.user_service.auth.Dto;

/**
 * Data Transfer Object for User information.
 * Used to transfer user data from server layers.
 */
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

    public UserResponse(Builder builder) {
        this.userName = builder.userName;
        this.email = builder.email;
        this.userRole = builder.userRole;
        this.id = builder.id;
    }

    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRole() {
        return userRole;
    }

    public static class Builder {
        private Long id;
        private String userName;
        private String email;
        private String userRole;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder userRole(String userRole) {
            this.userRole = userRole;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
        }
    }
}
