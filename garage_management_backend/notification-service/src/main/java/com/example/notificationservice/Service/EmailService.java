package com.example.notificationservice.Service;

import com.example.notificationservice.dtos.MaintenanceNotificationDTO;
import com.example.notificationservice.dtos.ClientDTO;
import com.example.notificationservice.dtos.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.messaging.handler.annotation.Payload;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.util.Assert;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@EnableKafka
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final ClientServiceClient clientServiceClient;
    private final VehicleServiceClient vehicleServiceClient;

    /**
     * Kafka listener for maintenance completion notifications.
     *
     * @param notification the maintenance notification received from the Kafka topic
     * @param acknowledgment Kafka message acknowledgment
     */
    @KafkaListener(
        topics = "maintenance-scheduled", 
        groupId = "notification-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    
    public void handleMaintenanceCompletionNotification(
            @Payload MaintenanceNotificationDTO notification, 
        Acknowledgment acknowledgment
    ) {
        try {
            // Validate input
            validateNotification(notification);

            // Send notification
            sendMaintenanceCompletionNotification(notification);

            // Acknowledge message processing
            acknowledgment.acknowledge();
            
            log.info("Processed maintenance completion notification for vehicle: {}", 
                     notification.getVehicleId());
        } catch (Exception e) {
            log.error("Error processing maintenance completion notification", e);
            // Consider implementing a dead letter queue or retry mechanism
            acknowledgment.acknowledge(); // Prevent infinite retries
        }
    }

    /**
     * Validates the maintenance notification DTO
     *
     * @param notification Maintenance notification to validate
     */
    private void validateNotification(MaintenanceNotificationDTO notification) {
        Assert.notNull(notification, "Maintenance notification cannot be null");
        Assert.hasText(notification.getVehicleId(), "Vehicle ID must not be empty");
        Assert.notNull(notification.getStartTime(), "Maintenance start time must not be null");
        Assert.notNull(notification.getEndTime(), "Maintenance end time must not be null");
    }

    /**
     * Sends an email notification for maintenance completion.
     *
     * @param notification the maintenance notification containing relevant information
     * @throws MessagingException if there's an error while composing or sending the email
     */
    public void sendMaintenanceCompletionNotification(MaintenanceNotificationDTO notification) throws MessagingException {
        // Retrieve vehicle and client information
        VehicleDTO vehicle = vehicleServiceClient.getVehicleById(notification.getVehicleId());
        ClientDTO client = clientServiceClient.getClientById(vehicle.getOwnerId());

        // Create email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set email details
        helper.setTo(client.getEmail()); // Assuming ClientDTO has an email field
        helper.setSubject("Maintenance programmée ");

        // Format vehicle information
        String vehicleInfo = String.format("%s %s (%s)",
            vehicle.getBrand(),
            vehicle.getModel(),
            vehicle.getRegistrationNumber()
        );

        // Format maintenance dates
        String formattedStartDate = formatDateTime(notification.getStartTime());
        String formattedEndDate = formatDateTime(notification.getEndTime());

        // Prepare email content
        String emailContent = String.format("""
            Cher/Chère %s,

            Nous vous confirmons que la maintenance de votre véhicule a été programmée :

            Véhicule : %s
            Date de début prévue : %s
            Date de fin prévue : %s
            Travaux à effectuer : %s
            Statut : %s

            Merci de prévoir de laisser votre véhicule à la date et l'heure indiquées.

            Cordialement,
            L'équipe du garage
            """,
            client.getFirstname()+" ",
            vehicleInfo,
            formattedStartDate,
            formattedEndDate,
            notification.getDescription(),
            notification.getStatus() != null ? notification.getStatus() : "Programmé"
        );

        helper.setText(emailContent);

        // Send email
        mailSender.send(message);
    }

    /**
     * Formats LocalDateTime to a readable string
     *
     * @param dateTime LocalDateTime to format
     * @return Formatted date string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}