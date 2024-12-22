package com.example.planification_service.Services;

import com.example.planification_service.Entities.MaintenanceJob;
import com.example.planification_service.Repositories.MaintenanceJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.planification_service.Services.VehicleServiceClient;
import com.example.planification_service.Services.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceJobService {

    private final MaintenanceJobRepository maintenanceJobRepository;
    @Autowired
    private final VehicleServiceClient vehicleServiceClient;

    /**
     * Retrieve all maintenance jobs.
     *
     * @return List of all MaintenanceJob entities.
     */
    public List<MaintenanceJob> getAllMaintenanceJobs() {
        return maintenanceJobRepository.findAll();
    }

    /**
     * Retrieve a maintenance job by its ID.
     *
     * @param id The ID of the maintenance job.
     * @return The MaintenanceJob entity if found.
     * @throws RuntimeException if the job is not found.
     */
    public MaintenanceJob getMaintenanceJobById(Long id) {
        return maintenanceJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance job with ID " + id + " not found"));
    }

    /**
     * Schedule a new maintenance job.
     * Fetches the `vehicleId` using the proxy before saving the job.
     *
     * @param maintenanceJob The MaintenanceJob to be scheduled.
     * @return The scheduled MaintenanceJob entity.
     */
    @Transactional
    public MaintenanceJob scheduleMaintenance(MaintenanceJob maintenanceJob) {
        // Fetch vehicle details using the Feign client
        VehicleDTO vehicleDTO = vehicleServiceClient.getVehicleById(maintenanceJob.getVehicleId().toString());

        if (vehicleDTO == null) {
            throw new RuntimeException("Vehicle with ID " + maintenanceJob.getVehicleId() + " not found");
        }

        return maintenanceJobRepository.save(maintenanceJob);
    }

    /**
     * Update an existing maintenance job.
     *
     * @param id             The ID of the maintenance job to be updated.
     * @param maintenanceJob The new MaintenanceJob details.
     * @return The updated MaintenanceJob entity.
     * @throws RuntimeException if the job is not found.
     */
    @Transactional
    public MaintenanceJob updateMaintenance(Long id, MaintenanceJob maintenanceJob) {
        MaintenanceJob existingJob = maintenanceJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance job with ID " + id + " not found"));

        // Update relevant fields
        existingJob.setDescription(maintenanceJob.getDescription());
        existingJob.setStatus(maintenanceJob.getStatus());
        existingJob.setStartTime(maintenanceJob.getStartTime());
        existingJob.setEndTime(maintenanceJob.getEndTime());

        return maintenanceJobRepository.save(existingJob);
    }

    /**
     * Delete a maintenance job by its ID.
     *
     * @param id The ID of the maintenance job to be deleted.
     */
    @Transactional
    public void deleteMaintenanceJob(Long id) {
        MaintenanceJob existingJob = maintenanceJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance job with ID " + id + " not found"));

        maintenanceJobRepository.delete(existingJob);
    }

}
