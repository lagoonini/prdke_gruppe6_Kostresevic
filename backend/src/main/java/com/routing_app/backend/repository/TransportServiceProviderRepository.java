package com.routing_app.backend.repository;

import com.routing_app.backend.model.TransportServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface TransportServiceProviderRepository extends JpaRepository<TransportServiceProvider, Integer> {
    Optional<TransportServiceProvider> findOneByEmailAndPassword(String email, String password);
    TransportServiceProvider findByEmail(String email);
}
