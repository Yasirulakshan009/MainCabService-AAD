package lk.ijse.MainCabService.controller;


import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> searchPayments(@PathVariable String id) {
        try {
            List<PaymentDTO> paymentDTOList = paymentService.searchPayments(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    paymentDTOList,
                    ResponseMessage.SUCCESS_MESSAGE
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllPayments() {
        try {
            List<PaymentDTO> paymentDTOList = paymentService.getAllPayments();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    paymentDTOList,
                    ResponseMessage.SUCCESS_MESSAGE
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

    @GetMapping(value = "/total-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getTotalRevenue() {
        try {
            double totalRevenue = paymentService.getTotalRevenue();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    totalRevenue,
                    ResponseMessage.SUCCESS_MESSAGE
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

    @GetMapping(value = "/monthly-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMonthlyRevenue() {
        try {
            double monthlyRevenue = paymentService.getMonthlyRevenue();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    monthlyRevenue,
                    ResponseMessage.SUCCESS_MESSAGE
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
