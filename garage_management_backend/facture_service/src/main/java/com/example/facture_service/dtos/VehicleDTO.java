package     com.example.facture_service.dtos;
import lombok.Data;

@Data
public class VehicleDTO {
    private String id;
    private String ownerId;
    private String brand;
    private String model;
    private String registrationNumber;
}
