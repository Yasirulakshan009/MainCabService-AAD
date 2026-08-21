package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.BookingDTO;
import lk.ijse.MainCabService.enumeratios.BookingStatus;

import java.util.List;

public interface BookingService {

    void saveBooking(BookingDTO bookingDTO);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> searchBooking(String keyword);

    void updateBookingStatus(Long id, BookingStatus status);

}
