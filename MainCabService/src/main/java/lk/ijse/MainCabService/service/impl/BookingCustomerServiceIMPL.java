package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.BookingCustomerDTO;
import lk.ijse.MainCabService.entity.BookingCustomer;
import lk.ijse.MainCabService.repository.BookingCustomerRepository;
import lk.ijse.MainCabService.service.BookingCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingCustomerServiceIMPL implements BookingCustomerService {

    private final BookingCustomerRepository bookingCustomerRepository;

    @Override
    public void saveBookingCustomer(BookingCustomerDTO bookingCustomerDTO) {

        log.info("Executing save method!");

        try{

            BookingCustomer bookingCustomer = new BookingCustomer();

            bookingCustomer.setBookingCustomerName(bookingCustomerDTO.getBookingCustomerName());
            bookingCustomer.setBookingCustomerRegisterDate(LocalDate.now());
            bookingCustomer.setBookingCustomerEmail(bookingCustomerDTO.getBookingCustomerEmail());
            bookingCustomer.setBookingCustomerNumber(bookingCustomerDTO.getBookingCustomerNumber());
            bookingCustomer.setBookingCustomerLicenseNumber(bookingCustomerDTO.getBookingCustomerLicenseNumber());

            bookingCustomerRepository.save(bookingCustomer);
            log.info("booking customer saved successfully!");

        } catch (Exception e) {
            log.error("error in sve method!");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteBookingCustomer(Long id) {

        log.info("executing delete booking customer method!");

        try{

            Optional<BookingCustomer> bookingCustomerOptional = bookingCustomerRepository.findById(id);

            if (!bookingCustomerOptional.isPresent()){
                throw new RuntimeException("Booking customer not found id with" + id);
            }

            bookingCustomerRepository.deleteById(id);

        } catch (Exception e) {
            log.error("error in delete booking customer method!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingCustomerDTO> getAllBookingCustomers() {

        log.info("executing load Booking customers method!");

        try{

            List<BookingCustomer> bookingCustomerList = bookingCustomerRepository.findAll();
            List<BookingCustomerDTO> bookingCustomerDTOList = new ArrayList<>();

            for(BookingCustomer bookingCustomer : bookingCustomerList){

                BookingCustomerDTO bookingCustomerDTO = new BookingCustomerDTO();

                bookingCustomerDTO.setBookingCustomerID(bookingCustomer.getBookingCustomerID());
                bookingCustomerDTO.setBookingCustomerName(bookingCustomer.getBookingCustomerName());
                bookingCustomerDTO.setBookingCustomerRegisterDate(bookingCustomer.getBookingCustomerRegisterDate());
                bookingCustomerDTO.setBookingCustomerEmail(bookingCustomer.getBookingCustomerEmail());
                bookingCustomerDTO.setBookingCustomerNumber(bookingCustomer.getBookingCustomerNumber());
                bookingCustomerDTO.setBookingCustomerLicenseNumber(bookingCustomer.getBookingCustomerLicenseNumber());

                bookingCustomerDTOList.add(bookingCustomerDTO);
            }

            log.info("add "+ bookingCustomerDTOList.size() + "booking customers!");
            return bookingCustomerDTOList;

        } catch (Exception e) {
            log.error("error in load booking customers!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BookingCustomerDTO> searchBookingCustomers(String keyword) {
        log.info("executing search Booking customers method!");

        try{

            List<BookingCustomer> bookingCustomerList = bookingCustomerRepository.searchBookingCustomers(keyword);
            List<BookingCustomerDTO> bookingCustomerDTOList = new ArrayList<>();

            for(BookingCustomer bookingCustomer : bookingCustomerList){

                BookingCustomerDTO bookingCustomerDTO = new BookingCustomerDTO();

                bookingCustomerDTO.setBookingCustomerID(bookingCustomer.getBookingCustomerID());
                bookingCustomerDTO.setBookingCustomerName(bookingCustomer.getBookingCustomerName());
                bookingCustomerDTO.setBookingCustomerRegisterDate(bookingCustomer.getBookingCustomerRegisterDate());
                bookingCustomerDTO.setBookingCustomerEmail(bookingCustomer.getBookingCustomerEmail());
                bookingCustomerDTO.setBookingCustomerNumber(bookingCustomer.getBookingCustomerNumber());
                bookingCustomerDTO.setBookingCustomerLicenseNumber(bookingCustomer.getBookingCustomerLicenseNumber());

                bookingCustomerDTOList.add(bookingCustomerDTO);
            }

            log.info("search "+ bookingCustomerDTOList.size() + "booking customers!");
            return bookingCustomerDTOList;

        } catch (Exception e) {
            log.error("error in search booking customers!");
            throw new RuntimeException(e);
        }
    }
}
