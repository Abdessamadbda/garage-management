package com.example.facture_service.Service;  // Corrected package name

import com.example.facture_service.dtos.ClientDTO;
import com.example.facture_service.dtos.MaintenanceNotificationDTO;
import com.example.facture_service.dtos.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@EnableKafka
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final ClientServiceClient clientServiceClient;
    private final VehicleServiceClient vehicleServiceClient;
    private final PdfService pdfService;

    @KafkaListener(
        topics = "maintenance-completed", 
        groupId = "facture-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )    
    public void processMaintenanceNotification(
        @Payload MaintenanceNotificationDTO maintenanceNotification, 
        Acknowledgment acknowledgment
    ) {
        try {
            // Validate input
            if (maintenanceNotification == null) {
                log.error("Received null maintenance notification");
                safeAcknowledge(acknowledgment);
                return;
            }

            // Retrieve vehicle and client details
            VehicleDTO vehicle = vehicleServiceClient.getVehicleById(maintenanceNotification.getVehicleId());
            if (vehicle == null) {
                log.error("No vehicle found for ID: {}", maintenanceNotification.getVehicleId());
                safeAcknowledge(acknowledgment);
                return;
            }

            ClientDTO client = clientServiceClient.getClientById(vehicle.getOwnerId());
            if (client == null) {
                log.error("No client found for ID: {}", vehicle.getOwnerId());
                safeAcknowledge(acknowledgment);
                return;
            }

            // Validate client email
            if (client.getEmail() == null || client.getEmail().isEmpty()) {
                log.error("Client email is missing for client ID: {}", client.getId());
                safeAcknowledge(acknowledgment);
                return;
            }

            // Generate PDF for maintenance notification
            byte[] pdfBytes = pdfService.generateMaintenanceNotificationPdf(maintenanceNotification);
            if (pdfBytes == null || pdfBytes.length == 0) {
                log.error("Failed to generate PDF for maintenance notification");
                safeAcknowledge(acknowledgment);
                return;
            }

            // Create and send the email
            sendMaintenanceNotificationEmail(client, pdfBytes);

            log.info("Email sent successfully to {}", client.getEmail());
            safeAcknowledge(acknowledgment);

        } catch (Exception e) {
            log.error("Failed to process and send maintenance notification email", e);
            safeAcknowledge(acknowledgment);
        }
    }

    private void sendMaintenanceNotificationEmail(ClientDTO client, byte[] pdfBytes) throws MessagingException {
        // Create and send the email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(client.getEmail());
        helper.setSubject("Maintenance Notification for Your Vehicle");
        helper.setText(buildEmailBody(client), true);

        // Attach the generated PDF
        helper.addAttachment("Maintenance_Notification.pdf", new ByteArrayResource(pdfBytes));

        // Send the email
        mailSender.send(message);
    }

    private String buildEmailBody(ClientDTO client) {
        return String.format(
            "Dear %s,\n\n" +
            "We hope this email finds you well. " +
            "Please find the attached maintenance details for your vehicle.\n\n" +
            "Best regards,\n" +
            "Your Maintenance Team", 
            client.getFirstname()
        );
    }

    private void safeAcknowledge(Acknowledgment acknowledgment) {
        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }
}