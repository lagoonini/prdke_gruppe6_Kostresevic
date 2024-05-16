package com.routing_app.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.routing_app.backend.model.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeocodingService {
    @Value("${openrouteservice.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openrouteservice.org").build();
    }

    public Mono<Coordinate> getCoordinates(String address) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocode/search")
                        .queryParam("api_key", apiKey)
                        .queryParam("text", address)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::parseCoordinates);
    }

    private Coordinate parseCoordinates(JsonNode json) {
        if (json.has("features") && json.get("features").size() > 0) {
            JsonNode coordinates = json.get("features").get(0).get("geometry").get("coordinates");
            double longitude = coordinates.get(0).asDouble();
            double latitude = coordinates.get(1).asDouble();
            return new Coordinate(latitude, longitude);
        } else {
            throw new RuntimeException("No coordinates found for the given address.");
        }
    }
}
