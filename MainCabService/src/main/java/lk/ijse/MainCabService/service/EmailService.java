package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.ContactMessageDTO;
import lk.ijse.MainCabService.entity.Booking;
import lk.ijse.MainCabService.entity.BookingCustomer;

public interface EmailService {

    void sendBookingNotificationEmail(Booking booking, BookingCustomer bookingCustomer);
    void sendContactMessageEmail(ContactMessageDTO contactMessageDTO);
}