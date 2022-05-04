package io.rently.imageservice.services;

import io.rently.imageservice.utils.Broadcaster;
import io.rently.imageservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {
    public static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${mailer.baseurl}")
    public void setBaseUrl(String baseUrl) {
        MailerService.BASE_URL = baseUrl;
    }

    public static void dispatchErrorToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Image service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Jwt.generateBearerToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);
        try {
            restTemplate.postForObject(BASE_URL + "api/v1/emails/dispatch/", body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
    }
}
