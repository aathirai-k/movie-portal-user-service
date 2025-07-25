package com.movie.portal.user_service.auth.Repository;

import com.movie.portal.user_service.auth.Entity.User;

import java.util.Optional;

/**
 * Data Access Object interface for User entity.
 * Defines methods to save and retrieve user data.
 */
public interface UserDaoRepository {
    /**
     * Saves a User entity in the database.
     *
     * @param user the User object to be saved
     * @return the saved User with generated ID
     */
    User saveUser(User user);

    /**
     * Finds a User by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Finds a User by user id.
     *
     * @param id the user id to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findUserById(Long id);

    /**
     * Update User.
     *
     * @param user update user information
     * @return integer value for updated user
     */
    int updateUser(User user);
}
