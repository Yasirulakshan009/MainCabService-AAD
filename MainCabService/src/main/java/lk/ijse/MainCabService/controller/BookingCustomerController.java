package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.BookingCustomerDTO;
import lk.ijse.MainCabService.dto.CustomerDTO;
import lk.ijse.MainCabService.service.BookingCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bookingCustomers")
@RequiredArgsConstructor
public class BookingCustomerController {

    private final BookingCustomerService bookingCustomerService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveBookingCustomer(@RequestBody BookingCustomerDTO bookingCustomerDTO) {
        bookingCustomerService.saveBookingCustomer(bookingCustomerDTO);
        return "Booking Customer saved successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteBookingCustomer(@PathVariable Long id){
        bookingCustomerService.deleteBookingCustomer(id);
        return "Booking Customer deleted successfully";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookingCustomerDTO> getAllBookingCustomers(){
        return bookingCustomerService.getAllBookingCustomers();
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookingCustomerDTO> searchBookingCustomers(@RequestParam String keyword) {
        return bookingCustomerService.searchBookingCustomers(keyword);
    }

}
