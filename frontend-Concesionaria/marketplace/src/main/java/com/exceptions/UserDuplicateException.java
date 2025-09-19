package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El usuario que se intenta agregar ya existe")
public class UserDuplicateException extends RuntimeException {

}