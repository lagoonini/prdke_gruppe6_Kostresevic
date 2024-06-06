package com.routing_app.backend.controller;

import com.routing_app.backend.login.TransportServiceProviderDTO;
import com.routing_app.backend.login.LoginDTO;
import com.routing_app.backend.model.TransportServiceProvider;
import com.routing_app.backend.repository.TransportServiceProviderRepository;
import com.routing_app.backend.service.TransportServiceProviderService;
import com.routing_app.backend.login.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/save")
    public ResponseEntity<TransportServiceProviderDTO> saveTransportServiceProvider(@RequestBody TransportServiceProviderDTO transportServiceProviderDTO) {
        TransportServiceProviderDTO savedTransportServiceProviderDTO = transportServiceProviderService.addTransportServiceProvider(transportServiceProviderDTO);
        return ResponseEntity.ok(savedTransportServiceProviderDTO);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, Object>> loginTransportServiceProvider(@RequestBody LoginDTO loginDTO) {
        LoginMessage loginResponse = transportServiceProviderService.loginTransportServiceProvider(loginDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", loginResponse.getMessage());

        if (loginResponse.isSuccess()) {
            TransportServiceProvider provider = transportServiceProviderRepository.findByEmail(loginDTO.getEmail());
            response.put("providerId", provider.getId());
        }
        return ResponseEntity.ok(response);
    }
}


