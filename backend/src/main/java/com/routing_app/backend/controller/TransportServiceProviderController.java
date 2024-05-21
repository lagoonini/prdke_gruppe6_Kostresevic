package com.routing_app.backend.controller;

import com.routing_app.backend.login.TransportServiceProviderDTO;
import com.routing_app.backend.login.LoginDTO;
import com.routing_app.backend.service.TransportServiceProviderService;
import com.routing_app.backend.login.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/tsp")
public class TransportServiceProviderController {

    @Autowired
    private TransportServiceProviderService transportServiceProviderService;

    @PostMapping(path = "/save")
    public ResponseEntity<TransportServiceProviderDTO> saveTransportServiceProvider(@RequestBody TransportServiceProviderDTO transportServiceProviderDTO) {
        TransportServiceProviderDTO savedTransportServiceProviderDTO = transportServiceProviderService.addTransportServiceProvider(transportServiceProviderDTO);
        return ResponseEntity.ok(savedTransportServiceProviderDTO);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginMessage> loginTransportServiceProvider(@RequestBody LoginDTO loginDTO) {
        LoginMessage loginResponse = transportServiceProviderService.loginTransportServiceProvider(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}


