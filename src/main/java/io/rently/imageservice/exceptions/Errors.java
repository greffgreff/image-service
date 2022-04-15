package io.rently.imageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException NO_IMAGE = new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");;
}
