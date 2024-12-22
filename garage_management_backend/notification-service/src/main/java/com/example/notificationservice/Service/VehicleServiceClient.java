package com.example.notificationservice.Service;

import com.example.notificationservice.dtos.VehicleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "vehicle-service")
public interface VehicleServiceClient {
    @GetMapping("/vehicles/{id}")
    VehicleDTO getVehicleById(@PathVariable("id") String id);
}
