package com.example.vehicleservice.Controller;

import com.example.vehicleservice.Entities.Vehicle;
import com.example.vehicleservice.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@RestController
@EnableKafka
@CrossOrigin(origins = "http://localhost:3000")

public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/vehicles/{id}")
    public Vehicle getVehicle(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping("/vehicles")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.addVehicle(vehicle);
        kafkaTemplate.send("vehicles-topic", "vehicle.created", createdVehicle.toString());
        return createdVehicle;
    }

    @PutMapping("/vehicles/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicle);
        kafkaTemplate.send("vehicles-topic", "vehicle.updated", updatedVehicle.toString());
        return updatedVehicle;
    }

    @KafkaListener(topics = "clients-topic", groupId = "vehicle-service-group")
    public void listenClientEvents(String message) {
        System.out.println("Received message in vehicle service: " + message);
        // Logic to process client events can be added here
    }
}
