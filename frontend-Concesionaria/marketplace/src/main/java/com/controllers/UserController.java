package com.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.entity.User;
import com.entity.dto.UserRequest;
import com.exceptions.UserDuplicateException;
import com.exceptions.UserNotFoundException;
import com.service.UserService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody UserRequest request) throws UserDuplicateException {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) throws UserNotFoundException {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> list() {
        return userService.list();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody UserRequest request) throws UserNotFoundException, UserDuplicateException {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws UserNotFoundException {
        userService.delete(id);
    }
}
