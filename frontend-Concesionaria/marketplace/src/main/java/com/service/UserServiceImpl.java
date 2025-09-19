package com.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.entity.Role;
import com.entity.User;
import com.entity.dto.UserRequest;
import com.exceptions.UserDuplicateException;
import com.exceptions.UserNotFoundException;
import com.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User create(UserRequest request) throws UserDuplicateException {
        final String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new UserDuplicateException();
        }
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new UserDuplicateException();
        }

        User u = new User();
        applyRequest(u, request, true);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Aca asignamos el rol de USER por defecto si no se especifica
        if (u.getRole() == null) {
            u.setRole(Role.USER);
        }

        return userRepository.save(u);
    }
    @Override
    @Transactional(readOnly = true)
    public User getById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) throws UserNotFoundException {
        final String normalizedEmail = normalizeEmail(email);
        return userRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(UUID id, UserRequest request) throws UserNotFoundException, UserDuplicateException {
        User u = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());

        // Si el email cambia hay que validarlo de nuevo
        if (request.getEmail() != null) {
            String newEmail = normalizeEmail(request.getEmail());
            if (!newEmail.equalsIgnoreCase(u.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new UserDuplicateException();
            }
            u.setEmail(newEmail);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            u.setPassword(request.getPassword());
        }

        applyRequest(u, request, false);

        return userRepository.save(u);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    private void applyRequest(User u, UserRequest r, boolean isCreate) {
        if (r.getName() != null)            u.setName(r.getName());
        if (r.getSurname() != null)         u.setSurname(r.getSurname());
        if (r.getPhone() != null)           u.setPhone(r.getPhone());
        if (r.getAddress() != null)         u.setAddress(r.getAddress());
        if (r.getCity() != null)            u.setCity(r.getCity());
        if (r.getState() != null)           u.setState(r.getState());
        if (r.getZip() != null)             u.setZip(r.getZip());
        if (r.getCountry() != null)         u.setCountry(r.getCountry());

        // Asignar rol si se especifica
        if (r.getRole() != null) {
            u.setRole(r.getRole());
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

}
