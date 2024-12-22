package     com.example.notificationservice.dtos;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class MaintenanceNotificationDTO {
    private String vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String status; // Add this if you're updating status
}