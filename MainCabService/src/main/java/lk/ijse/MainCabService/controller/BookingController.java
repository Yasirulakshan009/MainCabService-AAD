package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.BookingDTO;
import lk.ijse.MainCabService.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveBookings(@RequestBody  BookingDTO bookingDTO){
        try{
            bookingService.saveBooking(bookingDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.SAVE_SUCCESS
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllBookings(){
        List<BookingDTO> bookingDTOList = bookingService.getAllBookings();
        CommonResponse commonResponse = new CommonResponse(
                ResponseCode.OPERATION_SUCCESS,
                bookingDTOList,
                ResponseMessage.SUCCESS_MESSAGE
        );

        return new ResponseEntity<>(commonResponse,HttpStatus.OK);
    }

    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> searchBookings(@RequestParam String keyword){
        List<BookingDTO> bookingDTOList = bookingService.searchBooking(keyword);
        CommonResponse commonResponse = new CommonResponse(
                ResponseCode.OPERATION_SUCCESS,
                bookingDTOList,
                ResponseMessage.SUCCESS_MESSAGE
        );
        return new ResponseEntity<>(commonResponse,HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateBookingStatus(@PathVariable Long id, @RequestParam lk.ijse.MainCabService.enumeratios.BookingStatus status){
        try{
            bookingService.updateBookingStatus(id, status);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.UPDATE_SUCCESS
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
