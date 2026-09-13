package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.ContactMessageDTO;
import lk.ijse.MainCabService.entity.Booking;
import lk.ijse.MainCabService.entity.BookingCustomer;
import lk.ijse.MainCabService.entity.WebsiteSetting;
import lk.ijse.MainCabService.repository.WebsiteSettingRepository;
import lk.ijse.MainCabService.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceIMPL implements EmailService {

    private final JavaMailSender mailSender;
    private final WebsiteSettingRepository websiteSettingRepository;

    @Value("${app.notification.fallback-email}")
    private String fallbackEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void sendBookingNotificationEmail(Booking booking, BookingCustomer bookingCustomer) {

        log.info("Executing sendBookingNotificationEmail()");

        String toEmail = resolveNotificationEmail();

        String subject = "New Booking Received - " + bookingCustomer.getBookingCustomerName();

        String body =
                "A new booking has been made on the website.\n\n" +
                        "----- Customer Details -----\n" +
                        "Name           : " + bookingCustomer.getBookingCustomerName() + "\n" +
                        "Email          : " + bookingCustomer.getBookingCustomerEmail() + "\n" +
                        "Phone          : " + bookingCustomer.getBookingCustomerNumber() + "\n" +
                        "License Number : " + bookingCustomer.getBookingCustomerLicenseNumber() + "\n\n" +
                        "----- Booking Details -----\n" +
                        "Vehicle        : " + booking.getBookingVehicle() + "\n" +
                        "Start Date     : " + formatDate(booking.getStartDate()) + "\n" +
                        "End Date       : " + formatDate(booking.getEndDate()) + "\n" +
                        "Pickup Time : " + booking.getPickupTime() + "\n" +
                        "Return Time : " + booking.getReturnTime() + "\n" +
                        "Pickup Address : " + booking.getPickupAddress() + "\n" +
                        "Status         : " + booking.getBookingStatus() + "\n\n" +
                        "Please log in to the admin dashboard to review and confirm this booking.";

        sendEmail(toEmail, subject, body);
    }

    @Override
    public void sendContactMessageEmail(ContactMessageDTO contactMessageDTO) {

        log.info("Executing sendContactMessageEmail()");

        String toEmail = resolveNotificationEmail();

        String subject = "New Contact Message - " + contactMessageDTO.getSubject();

        String body =
                "A new message has been submitted through the website contact form.\n\n" +
                        "Name    : " + contactMessageDTO.getName() + "\n" +
                        "Email   : " + contactMessageDTO.getEmail() + "\n" +
                        "Phone   : " + (isBlank(contactMessageDTO.getPhone()) ? "Not provided" : contactMessageDTO.getPhone()) + "\n" +
                        "Subject : " + contactMessageDTO.getSubject() + "\n\n" +
                        "Message:\n" + contactMessageDTO.getMessage();

        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            log.info("Email sent successfully to: " + to);

        } catch (Exception e) {
            log.error("Error while sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private String resolveNotificationEmail() {

        Optional<WebsiteSetting> optionalSetting = websiteSettingRepository.findById(1L);

        if (optionalSetting.isPresent() && !isBlank(optionalSetting.get().getEmail())) {
            return optionalSetting.get().getEmail();
        }

        return fallbackEmail;
    }

    private String formatDate(java.time.LocalDate date) {
        return date == null ? "-" : date.format(DATE_FORMAT);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}