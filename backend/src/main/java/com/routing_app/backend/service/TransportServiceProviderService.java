package com.routing_app.backend.service;

import com.routing_app.backend.login.TransportServiceProviderDTO;
import com.routing_app.backend.login.LoginDTO;
import com.routing_app.backend.login.LoginMessage;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransportServiceProviderService {

    @Autowired
    private TransportServiceProviderRepository transportServiceProviderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public TransportServiceProviderDTO addTransportServiceProvider(TransportServiceProviderDTO transportServiceProviderDTO) {
        // Create a new TransportServiceProvider entity
        TransportServiceProvider transportServiceProvider = new TransportServiceProvider();
        transportServiceProvider.setCompanyName(transportServiceProviderDTO.getCompanyName());
        transportServiceProvider.setCompanyAddress(transportServiceProviderDTO.getCompanyAddress());
        transportServiceProvider.setEmail(transportServiceProviderDTO.getEmail());
        transportServiceProvider.setPassword(passwordEncoder.encode(transportServiceProviderDTO.getPassword()));

        // Save the TransportServiceProvider entity
        TransportServiceProvider savedTransportServiceProvider = transportServiceProviderRepository.save(transportServiceProvider);

        // Convert the saved entity back to a DTO to return
        TransportServiceProviderDTO savedTransportServiceProviderDTO = new TransportServiceProviderDTO();
        savedTransportServiceProviderDTO.setId(savedTransportServiceProvider.getId());
        savedTransportServiceProviderDTO.setCompanyName(savedTransportServiceProvider.getCompanyName());
        savedTransportServiceProviderDTO.setCompanyAddress(savedTransportServiceProvider.getCompanyAddress());
        savedTransportServiceProviderDTO.setEmail(savedTransportServiceProvider.getEmail());
        // Do not include the password in the returned DTO for security reasons

        return savedTransportServiceProviderDTO;
    }

    public LoginMessage loginTransportServiceProvider(LoginDTO loginDTO) {
        String msg = "";
        TransportServiceProvider transportServiceProvider = transportServiceProviderRepository.findByEmail(loginDTO.getEmail());
        if (transportServiceProvider != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = transportServiceProvider.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<TransportServiceProvider> transportServiceProviderOptional = transportServiceProviderRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (transportServiceProviderOptional.isPresent()) {
                    return new LoginMessage("Login Success", true, transportServiceProvider.getId());
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("Password Not Match", false);
            }
        } else {
            return new LoginMessage("Email not exists", false);
        }
    }
}

