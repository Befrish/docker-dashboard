package de.befrish.docker.dashboard.webclient;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientProvider {

    private final int requestTimeoutInMilliseconds;

    public WebClientProvider(final int requestTimeoutInMilliseconds) {
        this.requestTimeoutInMilliseconds = requestTimeoutInMilliseconds;
    }

    public WebClient createWebClient() {
//    public CloseableHttpClient createHttpClient() {
        final WebClient webClient = WebClient.create(); // TODO Timeout
        return webClient;
//        RequestConfig.Builder requestBuilder = RequestConfig.custom();
//        requestBuilder = requestBuilder.setConnectTimeout(requestTimeoutInMilliseconds);
//        requestBuilder = requestBuilder.setConnectionRequestTimeout(requestTimeoutInMilliseconds);
//
//        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
//        return httpClientBuilder.build();
    }

}
