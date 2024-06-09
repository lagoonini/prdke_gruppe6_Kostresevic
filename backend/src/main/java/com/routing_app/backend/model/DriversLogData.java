package com.routing_app.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "drivers_log_data")
public class DriversLogData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "log_addresses", joinColumns = @JoinColumn(name = "log_data_id"))
    @Column(name = "address")
    private List<String> addresses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private TransportServiceProvider transportServiceProvider;

    @Column(name = "invoice_status", nullable = false)
    private String invoiceStatus = "Nicht ausgestellt"; // Default status

    // Getters and setters
    public Long getId() {
        return id;
    }

    public TransportServiceProvider getTransportServiceProvider() {
        return transportServiceProvider;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public void setTransportServiceProvider(TransportServiceProvider transportServiceProvider) {
        this.transportServiceProvider = transportServiceProvider;
    }
}
