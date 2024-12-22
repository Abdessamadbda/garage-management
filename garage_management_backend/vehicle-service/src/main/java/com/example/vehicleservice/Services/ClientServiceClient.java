package com.example.vehicleservice.Services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


        
@FeignClient(name = "client-service")

public interface ClientServiceClient {

    @GetMapping("/clients/{id}")
    ClientDTO getClientById(@PathVariable("id") String id);
}
