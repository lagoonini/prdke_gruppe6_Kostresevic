package com.routing_app.backend.model;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator", strategy = "com.routing_app.backend.help_functions.CustomIdentifierGenerator")
    private Long id;

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private String vehicleName;

    @Column(nullable = false)
    private int seats;

    @Column(nullable = false)
    private boolean wheelchairAccessible;

    @Column(nullable = false)
    private String startPoint;

    @Column(nullable = false)
    private double startPointLatitude; // Added field

    @Column(nullable = false)
    private double startPointLongitude; // Added field

    @Column(nullable = false)
    private String endPoint;

    @Column(nullable = false)
    private double endPointLatitude; // Added field

    @Column(nullable = false)
    private double endPointLongitude; // Added field

    // Getters
    public Long getId() { return id; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleName() { return vehicleName; }
    public int getSeats() { return seats; }
    public boolean isWheelchairAccessible() { return wheelchairAccessible; }
    public String getStartPoint() { return startPoint; }
    public double getStartPointLatitude() { return startPointLatitude; } // Getter
    public double getStartPointLongitude() { return startPointLongitude; } // Getter
    public String getEndPoint() { return endPoint; }
    public double getEndPointLatitude() { return endPointLatitude; } // Getter
    public double getEndPointLongitude() { return endPointLongitude; } // Getter

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public void setSeats(int seats) { this.seats = seats; }
    public void setWheelchairAccessible(boolean wheelchairAccessible) { this.wheelchairAccessible = wheelchairAccessible; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    public void setStartPointLatitude(double startPointLatitude) { this.startPointLatitude = startPointLatitude; } // Setter
    public void setStartPointLongitude(double startPointLongitude) { this.startPointLongitude = startPointLongitude; } // Setter
    public void setEndPoint(String endPoint) { this.endPoint = endPoint; }
    public void setEndPointLatitude(double endPointLatitude) { this.endPointLatitude = endPointLatitude; } // Setter
    public void setEndPointLongitude(double endPointLongitude) { this.endPointLongitude = endPointLongitude; } // Setter
}
