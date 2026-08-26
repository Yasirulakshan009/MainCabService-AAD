package lk.ijse.MainCabService.controller;

import jakarta.validation.Valid;
import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.BookingCustomerDTO;
import lk.ijse.MainCabService.service.BookingCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bookingCustomers")
@RequiredArgsConstructor
public class BookingCustomerController {

    private final BookingCustomerService bookingCustomerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveBookingCustomer(@Valid @RequestBody BookingCustomerDTO bookingCustomerDTO) {

        try {
            bookingCustomerService.saveBookingCustomer(bookingCustomerDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.SAVE_SUCCESS
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
        } catch (Exception e) {

            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(commonResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteBookingCustomer(@PathVariable Long id){

        try{
            bookingCustomerService.deleteBookingCustomer(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.DELETE_SUCCESS
            );
            return new ResponseEntity<>(commonResponse,HttpStatus.OK);

        } catch (Exception e) {
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(commonResponse,HttpStatus.OK);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllBookingCustomers(){
        List<BookingCustomerDTO> bookingCustomerDTOList = bookingCustomerService.getAllBookingCustomers();
        CommonResponse commonResponse = new CommonResponse(
                ResponseCode.OPERATION_SUCCESS,
                bookingCustomerDTOList,
                ResponseMessage.SUCCESS_MESSAGE
        );
        return new ResponseEntity<>(commonResponse,HttpStatus.OK);

    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> searchBookingCustomers(@RequestParam String keyword) {
        List<BookingCustomerDTO> bookingCustomerDTOList = bookingCustomerService.searchBookingCustomers(keyword);
        CommonResponse commonResponse = new CommonResponse(
                ResponseCode.OPERATION_SUCCESS,
                bookingCustomerDTOList,
                ResponseMessage.SUCCESS_MESSAGE
        );
        return new ResponseEntity<>(commonResponse,HttpStatus.OK);
    }

}
