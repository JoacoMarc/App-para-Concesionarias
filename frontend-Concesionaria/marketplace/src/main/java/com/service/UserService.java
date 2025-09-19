package com.service;

import java.util.List;
import java.util.UUID;

import com.entity.User;
import com.entity.dto.UserRequest;
import com.exceptions.UserDuplicateException;
import com.exceptions.UserNotFoundException;

public interface UserService {
    User create(UserRequest request) throws UserDuplicateException;
    User getById(UUID userId) throws UserNotFoundException;
    User getByEmail(String email) throws UserNotFoundException;
    List<User> list();
    User update(UUID userId, UserRequest request) throws UserNotFoundException, UserDuplicateException;
    void delete(UUID userId) throws UserNotFoundException;
}
