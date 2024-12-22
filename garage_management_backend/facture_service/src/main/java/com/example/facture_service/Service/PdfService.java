package com.example.facture_service.Service;

import com.example.facture_service.dtos.ClientDTO;
import com.example.facture_service.dtos.MaintenanceNotificationDTO;
import com.example.facture_service.dtos.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {

    private final ClientServiceClient clientServiceClient;
    private final VehicleServiceClient vehicleServiceClient;

    /**
     * Generates a PDF for the maintenance notification.
     * No Kafka involvement; this method is called directly to generate the PDF.
     *
     * @param maintenanceNotification the maintenance notification DTO
     * @return byte array of generated PDF
     */
    public byte[] generateMaintenanceNotificationPdf(MaintenanceNotificationDTO maintenanceNotification) {
        try {
            // Log the received notification
            log.info("Received maintenance notification: {}", maintenanceNotification);

            // Validate input
            if (maintenanceNotification == null) {
                log.error("Received null maintenance notification");
                return null;
            }

            // Get vehicle details from maintenance notification
            VehicleDTO vehicle = vehicleServiceClient.getVehicleById(maintenanceNotification.getVehicleId());

            // Get client details using the owner ID from the vehicle
            ClientDTO client = clientServiceClient.getClientById(vehicle.getOwnerId());

            // Create a new PDF document
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                // Create a new content stream for writing content
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Set up initial positioning
                    float yPosition = page.getMediaBox().getHeight() - 50;
                    float margin = 50;

                    // Title
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    centerText(contentStream, "Maintenance Invoice", PDType1Font.HELVETICA_BOLD, 18, yPosition, page);
                    yPosition -= 40;

                    // Reset font for body text
                    contentStream.setFont(PDType1Font.HELVETICA, 12);

                    // Vehicle Information
                    yPosition = writeSection(contentStream, "Vehicle Information", 
                        new String[][]{
                            {"Brand:", vehicle.getBrand()},
                            {"Model:", vehicle.getModel()},
                            {"Registration Number:", vehicle.getRegistrationNumber()}
                        }, 
                        margin, yPosition, PDType1Font.HELVETICA_BOLD, PDType1Font.HELVETICA);

                    // Client Information
                    yPosition = writeSection(contentStream, "Client Information", 
                        new String[][]{
                            {"Name:", client.getFirstname() + " " + client.getLastName()},
                            {"Email:", client.getEmail()},
                            {"Phone:", client.getPhone()}
                        }, 
                        margin, yPosition - 20, PDType1Font.HELVETICA_BOLD, PDType1Font.HELVETICA);

                    // Maintenance Details
                    yPosition = writeSection(contentStream, "Maintenance Details", 
                        new String[][]{
                            {"Start Time:", maintenanceNotification.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)},
                            {"End Time:", maintenanceNotification.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)},
                            {"Description:", maintenanceNotification.getDescription()},
                            {"Status:", maintenanceNotification.getStatus()}
                        }, 
                        margin, yPosition - 20, PDType1Font.HELVETICA_BOLD, PDType1Font.HELVETICA);
                }

                // Save PDF to byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                document.save(baos);

                // Log success
                log.info("PDF generated successfully for maintenance notification");

                return baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("Error generating maintenance notification PDF", e);
            // Return null in case of error
            return null;
        }
    }

    // Helper method to write a section with a header and multiple key-value pairs
    private float writeSection(PDPageContentStream contentStream, String sectionTitle, 
                               String[][] details, float margin, float yPosition, 
                               PDType1Font headerFont, PDType1Font bodyFont) throws IOException {
        // Write section header
        contentStream.setFont(headerFont, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(sectionTitle);
        contentStream.endText();
        yPosition -= 20;

        // Write details
        contentStream.setFont(bodyFont, 12);
        for (String[] detail : details) {
            // Write key
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(detail[0]);
            contentStream.endText();

            // Write value
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + 150, yPosition);
            contentStream.showText(detail[1]);
            contentStream.endText();

            yPosition -= 20;
        }

        return yPosition;
    }

    // Helper method to center text
    private void centerText(PDPageContentStream contentStream, String text, 
                            PDType1Font font, float fontSize, 
                            float yPosition, PDPage page) throws IOException {
        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float startX = (page.getMediaBox().getWidth() - titleWidth) / 2;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(startX, yPosition);
        contentStream.showText(text);
        contentStream.endText();
    }
}
