package com.routing_app.backend.repository;

import com.routing_app.backend.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByVehicleId(Long vehicleId);

}
