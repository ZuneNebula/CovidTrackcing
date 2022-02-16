package com.stackroute.service;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User expert) throws UserAlreadyExistsException;

    List<User> getUsers();

    Optional<User> getUserByUsername(String username) throws UserNotFoundException;

    User getUserById(String id) throws UserNotFoundException;

    User updateUser(User user) throws UserNotFoundException;
}
