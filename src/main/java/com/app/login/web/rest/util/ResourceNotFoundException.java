package com.app.login.web.rest.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException
 * It will be thrown when the client sends a request to get the details of a resource that doesn’t exist.
 * @author Megadotnet
 * @date 2018/4/5
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException() {
        this("Resource not found!");
    }
    public ResourceNotFoundException(String message) {
        this(message, null);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
