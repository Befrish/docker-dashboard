package de.befrish.docker.dashboard.webclient;

import de.befrish.docker.dashboard.domain.ContainerApplicationData;
import de.befrish.docker.dashboard.service.ApplicationVersionResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.befrish.docker.dashboard.webclient.TextFormatVersionExtractor.TEXT_FORMAT;

@Service
@RequiredArgsConstructor
public class WebClientApplicationVersionResolver implements ApplicationVersionResolver {

    @NonNull
    private final WebClientProvider webClientProvider;

    @NonNull
    private final VersionExtractor versionExtractor;

    @Override
    public Mono<String> getApplicationVersion(final ContainerApplicationData containerApplicationData) {
        return containerApplicationData.getApplicationVersionUrl()
                .map(applicationVersionUrl -> {
                    final String applicationVersionFormat
                            = containerApplicationData.getApplicationVersionFormat().orElse(TEXT_FORMAT);
                    return requestVersion(applicationVersionUrl, applicationVersionFormat);
                })
                .orElse(Mono.empty());
    }

    private Mono<String> requestVersion(
            @NonNull final String applicationVersionUrl,
            @NonNull final String applicationVersionFormat) {
        return webClientProvider.createWebClient().get()
                .uri(applicationVersionUrl)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseContent -> versionExtractor.extractVersion(responseContent, applicationVersionFormat)
                        .map(Mono::just)
                        .orElse(Mono.empty()));
    }

}
