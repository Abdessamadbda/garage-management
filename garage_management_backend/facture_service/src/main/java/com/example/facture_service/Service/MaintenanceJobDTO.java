package com.example.facture_service.Service;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MaintenanceJobDTO {
    private String clientEmail;
    private String clientName;
    private String vehicleInfo;
    private LocalDateTime maintenanceDateTime;
    private String description;
    private String maintenanceId;
}