package eu.gaiax.sd.util;

import eu.gaiax.sd.model.ResultsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ProxyCall {

    public static <T> ResponseEntity<T> doGet(final WebClient srv, final HttpServletRequest request) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((s, strings) -> queryParams.addAll(s, List.of(strings)));

        final WebClient.RequestHeadersSpec<?> callBuilder = srv
                .get()
                .uri(builder ->
                        builder.path(request.getRequestURI())
                                .queryParams(queryParams).build());

        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String hn = headerNames.nextElement();
            callBuilder.header(hn, request.getHeader(hn));
        }

        return callBuilder.retrieve()
                .toEntity(new ParameterizedTypeReference<T>() {
                    //
                }).block();
    }

    public static <T, R> ResponseEntity<T> doPost(WebClient srv, HttpServletRequest request, R rqBody) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((s, strings) -> queryParams.addAll(s, List.of(strings)));

        WebClient.RequestBodySpec prep = srv
                .post()
                .uri(builder ->
                        builder.path(request.getRequestURI())
                                .queryParams(queryParams).build());

        WebClient.RequestHeadersSpec<?> callBuilder = prep;
        if (rqBody != null) {
            callBuilder = prep.bodyValue(rqBody);
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String hn = headerNames.nextElement();
            String header = request.getHeader(hn);
            callBuilder.header(hn, header);
        }
        return callBuilder
                .retrieve()
                .toEntity(new ParameterizedTypeReference<T>() {
                })
                .block();
    }

    public static ResultsResponse validateSdData(
            WebClient webClient, HttpServletRequest request, byte[] arr, String url) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((s, strings) -> queryParams.addAll(s, List.of(strings)));

        return webClient
                .post()
                .uri((builder -> {
                    URI build = builder.path(url)
                                       .queryParams(queryParams).build();
                    return build;
                }
                ))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(arr)
                .retrieve()
                .bodyToMono(ResultsResponse.class)
                .block();
    }

    private static String extractRequestBody(HttpServletRequest request) {
        try {
            Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
