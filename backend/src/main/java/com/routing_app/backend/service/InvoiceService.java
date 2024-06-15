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

import java.util.AbstractMap;
import java.util.List;
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
                    List<String> addresses = logData.getAddresses();
                    return Flux.fromIterable(addresses)
                            .flatMap(address -> geocodingService.getCoordinates(address)
                                    .map(coordinate -> new AbstractMap.SimpleEntry<>(coordinate, address)))
                            .collectList()
                            .flatMap(coordinateAddressPairs -> {
                                List<Coordinate> coordinates = coordinateAddressPairs.stream()
                                        .map(AbstractMap.SimpleEntry::getKey)
                                        .collect(Collectors.toList());
                                List<String> addressList = coordinateAddressPairs.stream()
                                        .map(AbstractMap.SimpleEntry::getValue)
                                        .collect(Collectors.toList());

                                Mono<Double> distanceMono = routingService.calculateRouteDistance(coordinates);
                                return distanceMono.map(distance -> {
                                    Invoice invoice = buildInvoice(logData, coordinates, addressList, distance, providerId);
                                    logData.setInvoiceStatus("Abgeschlossen");  // Update the status
                                    driversLogDataService.saveDriversLogData(logData, providerId).subscribe();  // Save the log data with updated status
                                    return invoice;  // Return the newly created invoice
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
