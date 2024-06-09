package com.routing_app.backend.service;

import com.routing_app.backend.model.DriversLogData;
import com.routing_app.backend.model.Route;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.repository.DriversLogDataRepository;
import com.routing_app.backend.repository.RouteRepository;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DriversLogDataService {
    @Autowired
    private DriversLogDataRepository driversLogDataRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TransportServiceProviderRepository transportServiceProviderRepository;

    public Mono<DriversLogData> saveDriversLogData(DriversLogData logData, Long providerId) {
        TransportServiceProvider provider = transportServiceProviderRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        logData.setTransportServiceProvider(provider);
        return Mono.just(driversLogDataRepository.save(logData));
    }

    public Mono<DriversLogData> findDriversLogDataById(Long id) {
        return Mono.fromCallable(() -> driversLogDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DriversLogData not found")));
    }

    public Flux<DriversLogData> findAllDriversLogData(Long providerId) {
        List<DriversLogData> logs = driversLogDataRepository.findByTransportServiceProviderId(providerId);
        return Flux.fromIterable(logs);
    }

    public Mono<Void> deleteDriversLogData(Long id) {
        return Mono.fromRunnable(() -> driversLogDataRepository.deleteById(id));
    }

    // Method to create and return mock DriversLogData
    public Mono<DriversLogData> createMockDriversLogData(Long providerId) {
        return Mono.fromSupplier(() -> {
            Route mockRoute = routeRepository.findById(1L).orElseGet(() -> {
                Route newRoute = new Route();
                newRoute.setVehicleName("Mock Vehicle");
                newRoute.setVehicleType("Sedan");
                newRoute.setStartPoint("123 Mock Start Point, Mock City");
                newRoute.setStartPointLatitude(34.0522);
                newRoute.setStartPointLongitude(-118.2437);
                newRoute.setEndPoint("789 Mock End Point, Test Town");
                newRoute.setEndPointLatitude(34.1522);
                newRoute.setEndPointLongitude(-118.3437);
                TransportServiceProvider provider = transportServiceProviderRepository.findById(providerId)
                        .orElseThrow(() -> new RuntimeException("Provider not found"));
                newRoute.setTransportServiceProvider(provider);
                return routeRepository.save(newRoute);
            });

            List<String> addresses = new ArrayList<>();
            addresses.add(mockRoute.getStartPoint()); // Start point address
            addresses.addAll(Arrays.asList("Freistädter Strasse 317, 4040 Linz", "Altenberger Strasse 69 Linz")); // Additional fictional address
            addresses.add(mockRoute.getEndPoint()); // End point address

            DriversLogData mockLogData = new DriversLogData();
            mockLogData.setAddresses(addresses);
            mockLogData.setRoute(mockRoute);

            return saveDriversLogData(mockLogData, providerId).block(); // Pass the providerId here
        });
    }
}
