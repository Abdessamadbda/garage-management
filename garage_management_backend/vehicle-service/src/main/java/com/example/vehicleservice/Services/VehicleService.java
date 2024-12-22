package com.example.vehicleservice.Services;

import com.example.vehicleservice.Entities.Vehicle;
import com.example.vehicleservice.Repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.vehicleservice.Services.ClientServiceClient;



@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ClientServiceClient clientServiceClient;

    /**
     * Retrieve all vehicles.
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * Retrieve a vehicle by its ID.
     */
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    /**
     * Add a new vehicle after validating the owner (clientId).
     */
    public Vehicle addVehicle(Vehicle vehicle) {
        validateOwner(vehicle.getOwnerId());
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update an existing vehicle.
     */
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Vehicle with ID " + id + " not found")
        );

        validateOwner(vehicle.getOwnerId());

        // Update fields
        existingVehicle.setVin(vehicle.getVin());
        existingVehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
        existingVehicle.setBrand(vehicle.getBrand());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setYear(vehicle.getYear());
        existingVehicle.setColor(vehicle.getColor());
        existingVehicle.setMileage(vehicle.getMileage());
        existingVehicle.setFuelType(vehicle.getFuelType());
        existingVehicle.setPurchaseDate(vehicle.getPurchaseDate());
        existingVehicle.setOwnerId(vehicle.getOwnerId());
        existingVehicle.setVehicleStatus(vehicle.getVehicleStatus());

        return vehicleRepository.save(existingVehicle);
    }

    /**
     * Delete a vehicle by its ID.
     */
    public void deleteVehicle(Long id) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Vehicle with ID " + id + " not found")
        );
        vehicleRepository.delete(existingVehicle);
    }
    
    /**
     * Validate the client (owner) existence using the Feign client.
     */
    private void validateOwner(Long ownerId) {
        if (clientServiceClient.getClientById(String.valueOf(ownerId)) == null) {
            throw new RuntimeException("Owner with ID " + ownerId + " not found");
        }
    }
}
