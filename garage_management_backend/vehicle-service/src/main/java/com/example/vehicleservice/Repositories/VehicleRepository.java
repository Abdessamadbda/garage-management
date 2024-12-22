package com.example.vehicleservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vehicleservice.Entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
}
