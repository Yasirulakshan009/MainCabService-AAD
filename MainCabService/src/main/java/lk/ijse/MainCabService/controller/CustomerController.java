package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.CustomerDTO;
import lk.ijse.MainCabService.dto.VehicleDTO;
import lk.ijse.MainCabService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.saveCustomer(customerDTO);
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
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.updateCustomer(customerDTO);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.DELETE_SUCCESS
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getCustomerById(@PathVariable Long id) {
        try {
            CustomerDTO customerDTO = customerService.getCustomerById(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    customerDTO,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllCustomers() {
        try {
            List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    customerDTOList,
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

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> searchCustomers(@RequestParam String keyword) {
        try {
            List<CustomerDTO> customerDTOList = customerService.searchCustomers(keyword);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    customerDTOList,
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
