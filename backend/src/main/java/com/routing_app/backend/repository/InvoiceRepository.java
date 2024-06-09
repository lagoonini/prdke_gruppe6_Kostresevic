package com.routing_app.backend.repository;

import com.routing_app.backend.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByTransportServiceProviderId(Long providerId);
}

