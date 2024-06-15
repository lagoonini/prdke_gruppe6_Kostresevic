package com.routing_app.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(nullable = false)
    private Double distance;

    private final Double ratePerKm = 10.0;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private Route route;

    @ElementCollection
    @CollectionTable(name = "invoice_coordinates", joinColumns = @JoinColumn(name = "invoice_id"))
    private List<Coordinate> coordinates = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "invoice_addresses", joinColumns = @JoinColumn(name = "invoice_id"))
    @Column(name = "address")
    private List<String> addresses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private TransportServiceProvider transportServiceProvider;

    // Constructors, getters, and setters
    public Invoice() {
    }

    public Double getTotalCost() {
        return distance * ratePerKm;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getDistance() {
        return distance;
    }

    public TransportServiceProvider getTransportServiceProvider() {
        return transportServiceProvider;
    }

    public void setDistance(Double distance) {
        this.distance = distance/1000;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Double getRatePerKm() {
        return ratePerKm;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public void setTransportServiceProvider(TransportServiceProvider transportServiceProvider) {
        this.transportServiceProvider = transportServiceProvider;
    }
}

