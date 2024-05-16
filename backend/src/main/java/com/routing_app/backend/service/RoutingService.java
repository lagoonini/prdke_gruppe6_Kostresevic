package com.routing_app.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.routing_app.backend.model.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RoutingService {
    private final WebClient webClient;
    @Value("${openrouteservice.api.key}")
    private String apiKey;

    public RoutingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openrouteservice.org").build();
    }

    public Mono<Double> calculateRouteDistance(List<Coordinate> coordinates) {
        return webClient.post()
                .uri("/v2/directions/driving-car")
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(buildRequestBody(coordinates)))
                .retrieve()
                .onStatus(status -> status.isError(), response -> response.bodyToMono(String.class).flatMap(error -> Mono.error(new RuntimeException("Error from OpenRouteService: " + error))))
                .bodyToMono(JsonNode.class)
                .map(this::parseDistance);
    }

    private String buildRequestBody(List<Coordinate> coordinates) {
        String coords = coordinates.stream()
                .map(coord -> String.format("[%f,%f]", coord.getLongitude(), coord.getLatitude()))
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        return String.format("{\"coordinates\":[%s]}", coords);
    }

    private Double parseDistance(JsonNode json) {
        if (json.has("routes") && json.get("routes").size() > 0) {
            JsonNode routes = json.get("routes").get(0);
            JsonNode summary = routes.get("summary");
            return summary.get("distance").asDouble();
        } else {
            throw new RuntimeException("No routes found in the response.");
        }
    }
}
