package com.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth.AuthenticationRequest;
import com.auth.AuthenticationResponse;
import com.config.JwtService;
import com.entity.Role;
import com.entity.User;
import com.entity.dto.UserRequest;
import com.exceptions.UserDuplicateException;
import com.exceptions.UserInvalidCredentialsException;
import com.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequest request) throws UserDuplicateException {
        // Asegurar que el rol sea USER por defecto para registros públicos
        if (request.getRole() == null) {
            request.setRole(Role.USER);
        }
        
        // Usar UserService para crear el usuario (ya incluye todas las validaciones)
        User user = userService.create(request);
        
        // Generar JWT token
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserInvalidCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UserInvalidCredentialsException();
        }
        
        try {
            var user = userService.getByEmail(request.getEmail());
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (UserNotFoundException e) {
            // Esto no debería pasar ya que AuthenticationManager ya validó las credenciales
            throw new RuntimeException("User not found after successful authentication", e);
        }
    }
}