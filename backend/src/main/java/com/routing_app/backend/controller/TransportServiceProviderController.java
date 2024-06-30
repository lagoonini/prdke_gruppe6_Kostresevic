package com.routing_app.backend.controller;

import com.routing_app.backend.login.TransportServiceProviderDTO;
import com.routing_app.backend.login.LoginDTO;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import com.routing_app.backend.service.TransportServiceProviderService;
import com.routing_app.backend.login.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/tsp")
public class TransportServiceProviderController {

    @Autowired
    private TransportServiceProviderService transportServiceProviderService;

    @Autowired
    private TransportServiceProviderRepository transportServiceProviderRepository;

    // Get Transport Service Provider by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransportServiceProviderDTO> getTransportServiceProviderById(@PathVariable Long id) {
        try {
            TransportServiceProvider provider = transportServiceProviderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found"));
            TransportServiceProviderDTO dto = new TransportServiceProviderDTO(
                    provider.getId(),
                    provider.getCompanyName(),
                    provider.getCompanyAddress(),
                    provider.getEmail(),
                    null
            );
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Save Transport Service Provider
    @PostMapping(path = "/save")
    public ResponseEntity<TransportServiceProviderDTO> saveTransportServiceProvider(@RequestBody TransportServiceProviderDTO transportServiceProviderDTO) {
        try {
            TransportServiceProviderDTO savedTransportServiceProviderDTO = transportServiceProviderService.addTransportServiceProvider(transportServiceProviderDTO);
            return new ResponseEntity<>(savedTransportServiceProviderDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login Transport Service Provider
    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, Object>> loginTransportServiceProvider(@RequestBody LoginDTO loginDTO) {
        try {
            LoginMessage loginResponse = transportServiceProviderService.loginTransportServiceProvider(loginDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", loginResponse.getMessage());

            if (loginResponse.isSuccess()) {
                TransportServiceProvider provider = transportServiceProviderRepository.findByEmail(loginDTO.getEmail());
                if (provider != null) {
                    response.put("providerId", provider.getId());
                    return ResponseEntity.ok(response);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
