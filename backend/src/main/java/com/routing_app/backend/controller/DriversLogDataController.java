package com.routing_app.backend.controller;

import com.routing_app.backend.model.DriversLogData;
import com.routing_app.backend.model.Invoice;
import com.routing_app.backend.service.InvoiceService;
import com.routing_app.backend.service.DriversLogDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/drivers-log")
public class DriversLogDataController {
    @Autowired
    private DriversLogDataService driversLogDataService;
    @Autowired
    private InvoiceService invoiceService;

    // Endpoint to create a new DriversLogData
    @PostMapping("/")
    public Mono<ResponseEntity<DriversLogData>> createDriversLogData(@RequestParam Long providerId, @RequestBody DriversLogData logData) {
        return driversLogDataService.saveDriversLogData(logData, providerId)
                .map(savedLogData -> ResponseEntity.ok(savedLogData));
    }

    // Endpoint to retrieve a specific DriversLogData by ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DriversLogData>> getDriversLogDataById(@PathVariable Long id) {
        return driversLogDataService.findDriversLogDataById(id)
                .map(logData -> ResponseEntity.ok(logData))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Endpoint to retrieve all DriversLogData
    @GetMapping("/")
    public Flux<DriversLogData> getAllDriversLogData(@RequestParam Long providerId) {
        return driversLogDataService.findAllDriversLogData(providerId);
    }

    // Endpoint to update a specific DriversLogData
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DriversLogData>> updateDriversLogData(@PathVariable Long id, @RequestParam Long providerId, @RequestBody DriversLogData logData) {
        return driversLogDataService.findDriversLogDataById(id)
                .flatMap(storedLogData -> {
                    storedLogData.setAddresses(logData.getAddresses());
                    storedLogData.setRoute(logData.getRoute());
                    return driversLogDataService.saveDriversLogData(storedLogData, providerId);
                })
                .map(updatedLogData -> ResponseEntity.ok(updatedLogData))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a specific DriversLogData
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDriversLogData(@PathVariable Long id) {
        return driversLogDataService.deleteDriversLogData(id)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }

    // Endpoint to create mock DriversLogData including route's start and endpoint
    @PostMapping("/mock")
    public Mono<ResponseEntity<DriversLogData>> createMockDriversLogData(@RequestParam Long providerId) {
        return driversLogDataService.createMockDriversLogData(providerId)
                .map(mockLogData -> ResponseEntity.ok(mockLogData));
    }

    // Endpoint to generate an invoice from DriversLogData
    @PostMapping("/{id}/generate-invoice")
    public Mono<ResponseEntity<Invoice>> generateInvoiceFromDriversLog(@PathVariable Long id, @RequestParam Long providerId) {
        return invoiceService.createInvoiceFromLogData(id, providerId)
                .map(invoice -> ResponseEntity.ok(invoice))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
