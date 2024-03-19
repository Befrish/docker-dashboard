package de.befrish.docker.dashboard.config;

import de.befrish.docker.dashboard.webclient.WebClientProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfiguration {

    private static final int HTTP_REQUEST_TIMEOUT_IN_MILLISECONDS = 5000;

    @Bean
    WebClientProvider httpClientProvider() {
        return new WebClientProvider(HTTP_REQUEST_TIMEOUT_IN_MILLISECONDS);
    }

}
