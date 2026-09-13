package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.BookingDTO;
import lk.ijse.MainCabService.entity.Booking;
import lk.ijse.MainCabService.entity.BookingCustomer;
import lk.ijse.MainCabService.enumeratios.BookingStatus;
import lk.ijse.MainCabService.enumeratios.NotificationType;
import lk.ijse.MainCabService.repository.BookingCustomerRepository;
import lk.ijse.MainCabService.repository.BookingRepository;
import lk.ijse.MainCabService.service.BookingService;
import lk.ijse.MainCabService.service.EmailService;
import lk.ijse.MainCabService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceIMPL implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingCustomerRepository bookingCustomerRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Override
    public void saveBooking(BookingDTO bookingDTO) {

        log.info("Executing save Booking()");

        try{

            Booking booking = new Booking();

            booking.setBookingVehicle(bookingDTO.getVehicleModel());
            booking.setStartDate(bookingDTO.getStartDate());
            booking.setEndDate(bookingDTO.getEndDate());
            booking.setPickupTime(bookingDTO.getPickupTime());
            booking.setReturnTime(bookingDTO.getReturnTime());
            booking.setPickupAddress(bookingDTO.getPickupAddress());
            booking.setBookingStatus(BookingStatus.PENDING);

            BookingCustomer customer = bookingCustomerRepository.findById(bookingDTO.getBookingCustomerID())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + bookingDTO.getBookingCustomerID()));

            booking.setBookingCustomer(customer);


            bookingRepository.save(booking);
            log.info("Booking saved successfully with PENDING status!");

            notificationService.createNotification(
                    NotificationType.BOOKING,
                    "New booking from " + customer.getBookingCustomerName(),
                    booking.getBookingID()
            );

            try {
                emailService.sendBookingNotificationEmail(booking, customer);
            } catch (Exception emailException) {
                log.error("Booking saved but failed to send notification email: " + emailException.getMessage());
            }


        } catch (Exception e) {
            log.error("Error in save Booking(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingDTO> getAllBookings() {

        log.info("Executing getAllBookings()");

        try {
            List<Booking> bookingList = bookingRepository.findAll();
            List<BookingDTO> bookingDTOList = new ArrayList<>();

            for (Booking booking : bookingList) {
                BookingDTO dto = new BookingDTO();
                dto.setBookingID(booking.getBookingID());
                dto.setVehicleModel(booking.getBookingVehicle());
                dto.setStartDate(booking.getStartDate());
                dto.setEndDate(booking.getEndDate());
                dto.setPickupAddress(booking.getPickupAddress());
                dto.setBookingStatus(booking.getBookingStatus());

                if (booking.getBookingCustomer() != null) {
                    dto.setBookingCustomerID(booking.getBookingCustomer().getBookingCustomerID());
                    dto.setBookingCustomerName(booking.getBookingCustomer().getBookingCustomerName());
                }

                bookingDTOList.add(dto);
            }

            return bookingDTOList;

        } catch (Exception e) {
            log.error("Error in getAllBookings(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingDTO> searchBooking(String keyword) {
        log.info("Executing searchBookings() with keyword: " + keyword);

        try {
            List<Booking> bookingList = bookingRepository.searchBookings(keyword);
            List<BookingDTO> bookingDTOList = new ArrayList<>();

            for (Booking booking : bookingList) {
                BookingDTO dto = new BookingDTO();
                dto.setBookingID(booking.getBookingID());
                dto.setVehicleModel(booking.getBookingVehicle());
                dto.setStartDate(booking.getStartDate());
                dto.setEndDate(booking.getEndDate());
                dto.setPickupAddress(booking.getPickupAddress());
                dto.setBookingStatus(booking.getBookingStatus());

                if (booking.getBookingCustomer() != null) {
                    dto.setBookingCustomerID(booking.getBookingCustomer().getBookingCustomerID());
                    dto.setBookingCustomerName(booking.getBookingCustomer().getBookingCustomerName());
                }

                bookingDTOList.add(dto);
            }

            return bookingDTOList;

        } catch (Exception e) {
            log.error("Error in searchBookings(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBookingStatus(Long id, BookingStatus status) {

        log.info("Executing updateBookingStatus() for ID: " + id + " to " + status);

        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

            booking.setBookingStatus(status);
            bookingRepository.save(booking);
            log.info("Booking status updated successfully!");

        } catch (Exception e) {
            log.error("Error in updateBookingStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
