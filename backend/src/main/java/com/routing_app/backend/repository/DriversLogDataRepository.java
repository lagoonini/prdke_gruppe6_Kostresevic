package com.routing_app.backend.repository;

import com.routing_app.backend.model.DriversLogData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriversLogDataRepository extends JpaRepository<DriversLogData, Long> {
}
