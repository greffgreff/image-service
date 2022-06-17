package io.rently.imageservice.components;

import io.rently.imageservice.utils.Broadcaster;
import io.rently.imageservice.utils.Jwt;
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

    private final RestTemplate restTemplate;
    private final Jwt jwt;
    private final String endPointUrl;

    public MailerService(Jwt jwt, String baseUrl, RestTemplate restTemplate) {
        this.jwt = jwt;
        this.endPointUrl = baseUrl + "api/v1/emails/dispatch/";
        this.restTemplate = restTemplate;
    }

    public void dispatchErrorToDevs(Exception exception) {
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
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);
        try {
            restTemplate.postForObject(endPointUrl, body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
    }
}
