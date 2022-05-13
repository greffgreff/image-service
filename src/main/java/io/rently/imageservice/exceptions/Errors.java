package io.rently.imageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException NO_IMAGE = new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
    public static final ResponseStatusException INVALID_REQUEST = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request made");
    public static final ResponseStatusException UNAUTHORIZED_REQUEST = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Request is either no longer valid or has been tampered with");
    public static final ResponseStatusException INVALID_DATA = new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Image data not base 64 oncoded");
}
