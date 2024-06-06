package com.routing_app.backend.service;

import com.routing_app.backend.model.Route;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.model.Vehicle;
import com.routing_app.backend.repository.RouteRepository;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import com.routing_app.backend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TransportServiceProviderRepository transportServiceProviderRepository;

    // Retrieve all vehicles from the database
    public List<Vehicle> getAllVehicles(Long providerId) {
        return vehicleRepository.findByTransportServiceProviderId(providerId);
    }

    // Retrieve a vehicle by its ID
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    // Save a new vehicle or update an existing one
    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle, Long providerId) {
        TransportServiceProvider provider = transportServiceProviderRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        vehicle.setTransportServiceProvider(provider);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Check if a route already exists for this vehicle
        Optional<Route> existingRoute = routeRepository.findByVehicleId(savedVehicle.getId());
        if (!existingRoute.isPresent()) {
            // Create and save associated route if it doesn't exist
            Route route = new Route();
            route.setVehicle(savedVehicle);
            route.setTransportServiceProvider(provider); // Set the provider for the route
            route.setVehicleName(savedVehicle.getVehicleName());
            route.setVehicleType(savedVehicle.getVehicleType());
            route.setStartPoint(savedVehicle.getStartPoint());
            route.setStartPointLatitude(savedVehicle.getStartPointLatitude());
            route.setStartPointLongitude(savedVehicle.getStartPointLongitude());
            route.setEndPoint(savedVehicle.getEndPoint());
            route.setEndPointLatitude(savedVehicle.getEndPointLatitude());
            route.setEndPointLongitude(savedVehicle.getEndPointLongitude());
            routeRepository.save(route);
        }

        return savedVehicle; // Return the saved vehicle
    }

    // Update an existing vehicle
    @Transactional
    public Optional<Vehicle> updateVehicle(Long id, Vehicle vehicleDetails) {
        return vehicleRepository.findById(id).map(vehicle -> {
            // Update vehicle details
            vehicle.setVehicleType(vehicleDetails.getVehicleType());
            vehicle.setVehicleName(vehicleDetails.getVehicleName());
            vehicle.setSeats(vehicleDetails.getSeats());
            vehicle.setWheelchairAccessible(vehicleDetails.isWheelchairAccessible());
            vehicle.setStartPoint(vehicleDetails.getStartPoint());
            vehicle.setStartPointLatitude(vehicleDetails.getStartPointLatitude());
            vehicle.setStartPointLongitude(vehicleDetails.getStartPointLongitude());
            vehicle.setEndPoint(vehicleDetails.getEndPoint());
            vehicle.setEndPointLatitude(vehicleDetails.getEndPointLatitude());
            vehicle.setEndPointLongitude(vehicleDetails.getEndPointLongitude());

            // Save updated vehicle
            Vehicle updatedVehicle = vehicleRepository.save(vehicle);

            // Update associated route
            routeRepository.findByVehicleId(id).ifPresent(route -> {
                route.setVehicleName(updatedVehicle.getVehicleName());
                route.setVehicleType(updatedVehicle.getVehicleType());
                route.setStartPoint(updatedVehicle.getStartPoint());
                route.setStartPointLatitude(updatedVehicle.getStartPointLatitude());
                route.setStartPointLongitude(updatedVehicle.getStartPointLongitude());
                route.setEndPoint(updatedVehicle.getEndPoint());
                route.setEndPointLatitude(updatedVehicle.getEndPointLatitude());
                route.setEndPointLongitude(updatedVehicle.getEndPointLongitude());
                routeRepository.save(route);
            });

            return updatedVehicle;
        });
    }

    // Delete a vehicle by its ID
    public boolean deleteVehicle(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    routeRepository.findByVehicleId(vehicle.getId()).ifPresent(route -> routeRepository.delete(route));
                    vehicleRepository.delete(vehicle);
                    return true;
                }).orElse(false);
    }
}
