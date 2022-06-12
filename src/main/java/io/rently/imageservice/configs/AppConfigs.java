package io.rently.imageservice.configs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.imageservice.middlewares.Interceptor;
import io.rently.imageservice.components.MailerService;
import io.rently.imageservice.utils.Broadcaster;
import io.rently.imageservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfigs implements WebMvcConfigurer {

    @Value("${server.secret}")
    public String secret;
    @Value("${server.algo}")
    public SignatureAlgorithm algo;
    public RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://rently-io.herokuapp.com", "https://listing-service-rently.herokuapp.com/")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(new Jwt(this.secret, this.algo), RequestMethod.GET));
    }

    @Bean
    public Jwt jwt() {
        Broadcaster.info("Loaded service middleware with secret `" + this.secret + "`");
        Broadcaster.info("Loaded service middleware with algo `" + this.algo + "`");
        return new Jwt(this.secret, this.algo);
    }

    @Bean
    public MailerService mailerService(
            @Value("${mailer.secret}") String secret,
            @Value("${mailer.algo}") SignatureAlgorithm algo,
            @Value("${mailer.baseurl}") String baseUrl
    ) {
        Broadcaster.info("Loaded MailerService with base URL `" + baseUrl + "`");
        Broadcaster.info("Loaded MailerService with secret `" + secret + "`");
        Broadcaster.info("Loaded MailerService with algo `" + algo + "`");
        return new MailerService(new Jwt(secret, algo), baseUrl, this.restTemplate);
    }

}
