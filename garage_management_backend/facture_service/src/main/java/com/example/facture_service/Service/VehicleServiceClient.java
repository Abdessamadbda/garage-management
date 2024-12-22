package com.example.facture_service.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.facture_service.dtos.VehicleDTO;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "vehicle-service")
public interface VehicleServiceClient {

    @GetMapping("/vehicles/{id}")
    VehicleDTO getVehicleById(@PathVariable("id") String id);
}
