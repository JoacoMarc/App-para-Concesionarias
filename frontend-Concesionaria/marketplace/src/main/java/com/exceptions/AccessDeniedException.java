package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No tienes permisos para realizar esta acción. Se requiere rol de administrador.")
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("No tienes permisos para realizar esta acción. Se requiere rol de administrador.");
    }
    
    public AccessDeniedException(String message) {
        super(message);
    }
}
