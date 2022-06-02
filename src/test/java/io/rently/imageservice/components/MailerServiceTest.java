package io.rently.imageservice.components;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.imageservice.configs.BugsnagTestConfigs;
import io.rently.imageservice.utils.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@WebMvcTest(MailerService.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class MailerServiceTest {

    public MailerService mailerService;
    public static final String URL = "/";
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public RestTemplate restTemplate;
    public Jwt jwt;

    @BeforeEach
    void setup() {
        jwt = new Jwt(SECRET, ALGORITHM);
        restTemplate = Mockito.mock(RestTemplate.class);
        mailerService = new MailerService(jwt, URL, restTemplate);
    }

    @Test
    void dispatchErrorToDevs_dispatchedSuccess_void() {
        Exception exception = new Exception("My exception", new Throwable("My cause"));
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Image service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);

        assertDoesNotThrow(() -> mailerService.dispatchErrorToDevs(exception));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body_ -> {
                    assert body_.toString().contains("type=DEV_ERROR");
                    assert body_.toString().contains("datetime");
                    assert body_.toString().contains("message=" + exception.getMessage());
                    assert body_.toString().contains("cause=" + exception.getCause());
                    assert body_.toString().contains("trace=" + Arrays.toString(exception.getStackTrace()));
                    assert body_.toString().contains("service=Image service");
                    assert body_.toString().contains("exceptionType=" + exception.getClass());
                    assert body_.toString().contains("Authorization");
                    return true;
                }),
                Mockito.any()
        );
    }

    @Test
    void dispatchErrorToDevs_dispatchedFailed_doNotThrow() {
        Exception exception = new Exception("My exception", new Throwable("My cause"));
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Image service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);

        doThrow(new RestClientException("error")).when(restTemplate).postForObject(URL + "api/v1/emails/dispatch/", body, String.class);

        assertDoesNotThrow(() -> mailerService.dispatchErrorToDevs(exception));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body_ -> {
                    assert body_.toString().contains("type=DEV_ERROR");
                    assert body_.toString().contains("datetime");
                    assert body_.toString().contains("message=" + exception.getMessage());
                    assert body_.toString().contains("cause=" + exception.getCause());
                    assert body_.toString().contains("trace=" + Arrays.toString(exception.getStackTrace()));
                    assert body_.toString().contains("service=Image service");
                    assert body_.toString().contains("exceptionType=" + exception.getClass());
                    assert body_.toString().contains("Authorization");
                    return true;
                }),
                Mockito.any()
        );
    }
}