package com.routing_app.backend.service;

import com.routing_app.backend.model.Route;
import com.routing_app.backend.model.Vehicle;
import com.routing_app.backend.repository.RouteRepository;
import com.routing_app.backend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    // Retrieve all routes
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Retrieve a route by its ID
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    // Save a new route
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    // Update an existing route
    @Transactional
    public Optional<Route> updateRoute(Long id, Route routeDetails) {
        return routeRepository.findById(id).map(route -> {
            // Update route details
            route.setVehicleName(routeDetails.getVehicleName());
            route.setVehicleType(routeDetails.getVehicleType());
            route.setStartPoint(routeDetails.getStartPoint());
            route.setStartPointLatitude(routeDetails.getStartPointLatitude());
            route.setStartPointLongitude(routeDetails.getStartPointLongitude());
            route.setEndPoint(routeDetails.getEndPoint());
            route.setEndPointLatitude(routeDetails.getEndPointLatitude());
            route.setEndPointLongitude(routeDetails.getEndPointLongitude());

            // Check if the vehicle exists for the route
            if (route.getVehicle() != null) {
                Vehicle vehicle = route.getVehicle(); // Get the associated vehicle
                // Update vehicle details
                vehicle.setStartPoint(routeDetails.getStartPoint());
                vehicle.setStartPointLatitude(routeDetails.getStartPointLatitude());
                vehicle.setStartPointLongitude(routeDetails.getStartPointLongitude());
                vehicle.setEndPoint(routeDetails.getEndPoint());
                vehicle.setEndPointLatitude(routeDetails.getEndPointLatitude());
                vehicle.setEndPointLongitude(routeDetails.getEndPointLongitude());

                vehicleRepository.save(vehicle); // Save the updated vehicle
            }

            return routeRepository.save(route); // Save the updated route
        });
    }

    // Delete a route by its ID
    public boolean deleteRoute(Long id) {
        return routeRepository.findById(id)
                .map(route -> {
                    routeRepository.delete(route);
                    return true;
                }).orElse(false);
    }
}
