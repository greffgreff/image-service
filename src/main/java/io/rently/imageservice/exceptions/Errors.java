package io.rently.imageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException INTERNAL_SERVER_ERROR = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Request could not be completed");
    public static final ResponseStatusException NO_IMAGE = new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
    public static final ResponseStatusException INVALID_URI_PATH = new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Invalid or incomplete URI");
    public static final ResponseStatusException UNAUTHORIZED_REQUEST = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Request is either no longer valid or has been tampered with");
    public static final ResponseStatusException INVALID_DATA = new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Image data not base 64 oncoded");
    public static final ResponseStatusException EXPIRED_TOKEN = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is no longer valid");
    public static final ResponseStatusException MALFORMED_TOKEN = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is malformed or has been tampered with");
}
