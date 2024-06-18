package com.routing_app.backend.service;

import com.routing_app.backend.model.Coordinate;
import com.routing_app.backend.model.DriversLogData;
import com.routing_app.backend.model.Invoice;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.repository.InvoiceRepository;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private RoutingService routingService;
    @Autowired
    private DriversLogDataService driversLogDataService;
    @Autowired
    private TransportServiceProviderRepository transportServiceProviderRepository;

    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    public List<Invoice> getAllInvoices(Long providerId) {
        return invoiceRepository.findByTransportServiceProviderId(providerId);
    }

    public Invoice saveInvoice(Invoice invoice, Long providerId) {
        TransportServiceProvider provider = transportServiceProviderRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        invoice.setTransportServiceProvider(provider);
        return invoiceRepository.save(invoice);
    }

    public Mono<Invoice> createInvoiceFromLogData(Long logDataId, Long providerId) {
        return driversLogDataService.findDriversLogDataById(logDataId)
                .flatMap(logData -> {
                    // Ensure addresses are in the correct order
                    List<String> addresses = logData.getAddresses();

                    // Assign indices to each address
                    List<Map.Entry<Integer, String>> indexedAddresses = new ArrayList<>();
                    for (int i = 0; i < addresses.size(); i++) {
                        indexedAddresses.add(new AbstractMap.SimpleEntry<>(i, addresses.get(i)));
                    }

                    // Geocode addresses and pair them with their coordinates
                    return Flux.fromIterable(indexedAddresses)
                            .flatMap(entry -> geocodingService.getCoordinates(entry.getValue())
                                    .map(coordinate -> new AbstractMap.SimpleEntry<>(entry.getKey(), new AbstractMap.SimpleEntry<>(coordinate, entry.getValue()))))
                            .collectList()
                            .flatMap(coordinateAddressPairs -> {
                                // Sort by the original indices to maintain the order
                                coordinateAddressPairs.sort(Comparator.comparingInt(Map.Entry::getKey));

                                // Extract coordinates and addresses from pairs
                                List<Coordinate> coordinates = coordinateAddressPairs.stream()
                                        .map(pair -> pair.getValue().getKey())
                                        .collect(Collectors.toList());
                                List<String> addressList = coordinateAddressPairs.stream()
                                        .map(pair -> pair.getValue().getValue())
                                        .collect(Collectors.toList());

                                // Calculate the route distance
                                Mono<Double> distanceMono = routingService.calculateRouteDistance(coordinates);
                                return distanceMono.map(distance -> {
                                    // Build the invoice
                                    Invoice invoice = buildInvoice(logData, coordinates, addressList, distance, providerId);

                                    // Update the status of the log data
                                    logData.setInvoiceStatus("Abgeschlossen");

                                    // Save the log data with the updated status
                                    driversLogDataService.saveDriversLogData(logData, providerId).subscribe();

                                    // Return the newly created invoice
                                    return invoice;
                                });
                            });
                });
    }

    private Invoice buildInvoice(DriversLogData logData, List<Coordinate> coordinates, List<String> addresses, Double distance, Long providerId) {
        Invoice invoice = new Invoice();
        invoice.setRoute(logData.getRoute());
        invoice.setCoordinates(coordinates);
        invoice.setAddresses(addresses);
        invoice.setDistance(distance);
        return saveInvoice(invoice, providerId);
    }
}
