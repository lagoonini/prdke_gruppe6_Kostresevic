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

import java.util.List;

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

    // Method to create an invoice from DriversLogData
    public Mono<Invoice> createInvoiceFromLogData(Long logDataId, Long providerId) {
        return driversLogDataService.findDriversLogDataById(logDataId)
                .flatMap(logData -> {
                    List<String> addresses = logData.getAddresses();
                    return Flux.fromIterable(addresses)
                            .flatMap(geocodingService::getCoordinates)
                            .collectList()
                            .flatMap(coordinates -> {
                                Mono<Double> distanceMono = routingService.calculateRouteDistance(coordinates);
                                return distanceMono.map(distance -> {
                                    Invoice invoice = buildInvoice(logData, coordinates, distance, providerId);
                                    logData.setInvoiceStatus("Abgeschlossen");  // Update the status
                                    driversLogDataService.saveDriversLogData(logData, providerId).subscribe();  // Save the log data with updated status
                                    return invoice;  // Return the newly created invoice
                                });
                            });
                });
    }

    private Invoice buildInvoice(DriversLogData logData, List<Coordinate> coordinates, Double distance, Long providerId) {
        Invoice invoice = new Invoice();
        invoice.setRoute(logData.getRoute());
        invoice.setCoordinates(coordinates);
        invoice.setDistance(distance);
        return saveInvoice(invoice, providerId);
    }
}
