package com.routing_app.backend.controller;

import com.routing_app.backend.model.Invoice;
import com.routing_app.backend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        return invoice != null ? ResponseEntity.ok(invoice) : ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam Long providerId) {
        List<Invoice> invoices = invoiceService.getAllInvoices(providerId);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/")
    public ResponseEntity<Invoice> createInvoice(@RequestParam Long providerId, @RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.saveInvoice(invoice, providerId);
        return ResponseEntity.ok(savedInvoice);
    }

    @PostMapping("/{id}/generate-from-log")
    public Mono<ResponseEntity<Invoice>> generateInvoiceFromDriversLog(@PathVariable Long id, @RequestParam Long providerId) {
        return invoiceService.createInvoiceFromLogData(id, providerId)
                .map(invoice -> ResponseEntity.ok(invoice))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
