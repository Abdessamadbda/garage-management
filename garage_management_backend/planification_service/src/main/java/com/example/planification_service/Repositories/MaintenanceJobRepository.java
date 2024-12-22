package com.example.planification_service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.planification_service.Entities.MaintenanceJob;

@Repository
public interface MaintenanceJobRepository extends JpaRepository<MaintenanceJob, Long> {
    List<MaintenanceJob> findByVehicleId(Long vehicleId);
}
