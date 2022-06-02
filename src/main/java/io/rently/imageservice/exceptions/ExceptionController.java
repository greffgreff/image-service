package io.rently.imageservice.exceptions;

import com.bugsnag.Bugsnag;
import io.rently.imageservice.components.MailerService;
import io.rently.imageservice.utils.Broadcaster;
import io.rently.imageservice.dtos.ResponseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionController {

    @Autowired
    private Bugsnag bugsnag;
    @Autowired
    private MailerService mailer;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseContent unhandledException(HttpServletResponse response, Exception exception) {
        Broadcaster.error(exception);
        exception.printStackTrace();
        bugsnag.notify(exception);
        try {
            mailer.dispatchErrorToDevs(exception);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
        ResponseStatusException resEx = Errors.INTERNAL_SERVER_ERROR;
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getReason()).build();
    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        Broadcaster.httpError(ex);
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public static ResponseContent handleInvalidURI(HttpServletResponse response, MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        String msg = "Missing required query string parameter `" + ex.getParameterName() + "` of type "+
                ex.getParameterType() +" in URL. Example: '"+ ex.getParameterName() +"=<" + ex.getParameterType() + " value>'";
        Broadcaster.httpError(msg, status);
        return new ResponseContent.Builder(status).setMessage(msg).build();
    }

    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class })
    public static ResponseContent handleIllegalArgumentException(HttpServletResponse response, Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Broadcaster.httpError(ex.getMessage(), status);
        response.setStatus(status.value());
        return new ResponseContent.Builder(status).setMessage(ex.getMessage()).build();
    }
}
