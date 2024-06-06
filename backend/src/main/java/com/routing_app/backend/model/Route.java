package com.routing_app.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private String vehicleName;

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private String startPoint;

    @Column(nullable = false)
    private double startPointLatitude;

    @Column(nullable = false)
    private double startPointLongitude;

    @Column(nullable = false)
    private String endPoint;

    @Column(nullable = false)
    private double endPointLatitude;

    @Column(nullable = false)
    private double endPointLongitude;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private TransportServiceProvider transportServiceProvider;

    // Getters
    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public double getStartPointLatitude() {
        return startPointLatitude;
    }

    public double getStartPointLongitude() {
        return startPointLongitude;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public double getEndPointLatitude() {
        return endPointLatitude;
    }

    public double getEndPointLongitude() {
        return endPointLongitude;
    }

    public TransportServiceProvider getTransportServiceProvider() {
        return transportServiceProvider;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setStartPointLatitude(double startPointLatitude) {
        this.startPointLatitude = startPointLatitude;
    }

    public void setStartPointLongitude(double startPointLongitude) {
        this.startPointLongitude = startPointLongitude;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setEndPointLatitude(double endPointLatitude) {
        this.endPointLatitude = endPointLatitude;
    }

    public void setEndPointLongitude(double endPointLongitude) {
        this.endPointLongitude = endPointLongitude;
    }

    public void setTransportServiceProvider(TransportServiceProvider transportServiceProvider) {
        this.transportServiceProvider = transportServiceProvider;
    }
}
