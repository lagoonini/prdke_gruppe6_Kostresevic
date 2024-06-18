package com.routing_app.backend.controller;

import com.routing_app.backend.model.Invoice;
import com.routing_app.backend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Get a single invoice by ID
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        try {
            Invoice invoice = invoiceService.getInvoice(id);
            return invoice != null ? ResponseEntity.ok(invoice) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all invoices by provider ID
    @GetMapping("/")
    public ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam Long providerId) {
        try {
            List<Invoice> invoices = invoiceService.getAllInvoices(providerId);
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new invoice
    @PostMapping("/")
    public ResponseEntity<Invoice> createInvoice(@RequestParam Long providerId, @RequestBody Invoice invoice) {
        try {
            Invoice savedInvoice = invoiceService.saveInvoice(invoice, providerId);
            return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Generate invoice from drivers log
    @PostMapping("/{id}/generate-from-log")
    public Mono<ResponseEntity<Invoice>> generateInvoiceFromDriversLog(@PathVariable Long id, @RequestParam Long providerId) {
        return invoiceService.createInvoiceFromLogData(id, providerId)
                .map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
