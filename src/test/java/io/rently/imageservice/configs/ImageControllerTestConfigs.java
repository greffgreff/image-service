package io.rently.imageservice.configs;

import io.rently.imageservice.interfaces.ImageRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ImageControllerTestConfigs {

    @Bean
    @Primary
    public ImageRepository userRepository() {
        return Mockito.mock(ImageRepository.class);
    }
}
