package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.BookingCustomerDTO;

import java.util.List;

public interface BookingCustomerService {

    void saveBookingCustomer(BookingCustomerDTO bookingCustomerDTO);

    void deleteBookingCustomer(Long id);

    List<BookingCustomerDTO> getAllBookingCustomers();

    List<BookingCustomerDTO> searchBookingCustomers(String keyword);

}
