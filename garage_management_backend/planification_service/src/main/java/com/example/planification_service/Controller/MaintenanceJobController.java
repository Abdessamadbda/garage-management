package com.example.planification_service.Controller;

import com.example.planification_service.Entities.MaintenanceJob;
import com.example.planification_service.Services.MaintenanceJobService;
import com.example.planification_service.Services.VehicleServiceClient;
import com.example.planification_service.Services.VehicleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.CrossOrigin;
 


@RestController
@RequestMapping("/maintenance-jobs")
@RequiredArgsConstructor
@EnableKafka
@CrossOrigin(origins = "http://localhost:3000")
public class MaintenanceJobController {

    private final MaintenanceJobService maintenanceJobService;
    private final VehicleServiceClient vehicleServiceClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public ResponseEntity<List<MaintenanceJob>> getAllMaintenanceJobs() {
        return ResponseEntity.ok(maintenanceJobService.getAllMaintenanceJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceJob> getMaintenanceJob(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceJobService.getMaintenanceJobById(id));
    }

    @PostMapping
    public ResponseEntity<MaintenanceJob> scheduleMaintenance(@RequestBody MaintenanceJob maintenanceJob) {
        // Validate vehicle existence using VehicleServiceClient
        VehicleDTO vehicle = vehicleServiceClient.getVehicleById(maintenanceJob.getVehicleId().toString());
        if (vehicle == null) {
            return ResponseEntity.badRequest().body(null);
        }

        MaintenanceJob scheduledJob = maintenanceJobService.scheduleMaintenance(maintenanceJob);

        // Send Kafka event
        try {
            String eventMessage = objectMapper.writeValueAsString(scheduledJob);
            kafkaTemplate.send("maintenance-scheduled", eventMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if ("Scheduled".equals(scheduledJob.getStatus())) {
            try {
                String eventMessage = objectMapper.writeValueAsString(scheduledJob);
                kafkaTemplate.send("maintenance-scheduled", eventMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(scheduledJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceJob> updateMaintenance(
            @PathVariable Long id,
            @RequestBody MaintenanceJob maintenanceJob) {
        MaintenanceJob updatedJob = maintenanceJobService.updateMaintenance(id, maintenanceJob);

        // If maintenance is completed, send a Kafka event
        if ("COMPLETED".equals(updatedJob.getStatus())) {
            try {
                String eventMessage = objectMapper.writeValueAsString(updatedJob);
                kafkaTemplate.send("maintenance-completed", eventMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        

        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceJob(@PathVariable Long id) {
        maintenanceJobService.deleteMaintenanceJob(id);
        return ResponseEntity.noContent().build();
    }
}
