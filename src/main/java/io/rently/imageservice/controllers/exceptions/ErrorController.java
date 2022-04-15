package io.rently.searchservice.exceptions;

import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseContent handleGenericException(HttpServletResponse response, Exception exception) {
        String msg = "An internal server error occurred. Request could not be completed";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Broadcaster.error(exception);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseContent.Builder(status).setMessage(msg).build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public static ResponseContent handleInvalidURI(HttpServletResponse response, MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        String msg = "Missing required query string parameter `" + ex.getParameterName() + "` of type "+
                ex.getParameterType() +" in URL. Example: '"+ ex.getParameterName() +"=<" + ex.getParameterType() + " value>'";
        Broadcaster.httpError(msg, status);
        return new ResponseContent.Builder(status).setMessage(msg).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        Broadcaster.httpError(ex);
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public static ResponseContent handleIllegalArgumentException(HttpServletResponse response, IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Broadcaster.httpError(ex.getMessage(), status);
        response.setStatus(status.value());
        return new ResponseContent.Builder(status).setMessage(ex.getMessage()).build();
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public static ResponseContent handleNullPointerException(HttpServletResponse response, NullPointerException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Broadcaster.httpError(ex.getMessage(), status);
        response.setStatus(status.value());
        return new ResponseContent.Builder(status).setMessage(ex.getMessage()).build();
    }
}
