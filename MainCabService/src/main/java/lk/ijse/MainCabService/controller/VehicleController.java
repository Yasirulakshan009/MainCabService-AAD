package lk.ijse.MainCabService.controller;

import jakarta.validation.Valid;
import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.VehicleDTO;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.enumeratios.WebCategory;
import lk.ijse.MainCabService.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/vehicles")
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicle(@Valid @ModelAttribute VehicleDTO vehicleDTO) {
        try{

            vehicleService.saveVehicle(vehicleDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.SAVE_SUCCESS
            );

            return new ResponseEntity<>(commonResponse,HttpStatus.CREATED);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicle(@Valid @ModelAttribute VehicleDTO vehicleDTO) {
        try {
            vehicleService.updateVehicle(vehicleDTO);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.UPDATE_SUCCESS
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<CommonResponse> deleteVehicle(@PathVariable Long id){
        try {
            vehicleService.deleteVehicle(id);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    ResponseMessage.DELETE_SUCCESS
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getVehicleById(@PathVariable Long id){
        try {
            VehicleDTO vehicleDTO = vehicleService.getVehicleById(id);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    vehicleDTO,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<CommonResponse> getAllVehicles(){
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    vehicles,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getVehiclesByStatus(@PathVariable VehicleStatus status){
        try {
            List<VehicleDTO> vehicles = vehicleService.getVehiclesByStatus(status);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    vehicles,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<CommonResponse> searchVehicle(@RequestParam String keyword){
        try {
            List<VehicleDTO> vehicles = vehicleService.searchVehicles(keyword);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    vehicles,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/count/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getVehicleCountByStatus(@PathVariable VehicleStatus status){
        try {
            long count = vehicleService.getVehicleCountByStatus(status);
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    count,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getTotalVehicleCount(){
        try {
            long totalCount = vehicleService.getTotalVehicleCount();
            CommonResponse response = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    totalCount,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/website-fleet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getWebsiteVehicles() {
        try {
            List<VehicleDTO> websiteVehicles = vehicleService.getWebsiteVehicles();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    websiteVehicles,
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

    @GetMapping(value = "/website/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getVehiclesByWebCategory(@PathVariable String category) {
        try {
            WebCategory webCategory = WebCategory.valueOf(category.toUpperCase());
            List<VehicleDTO> vehicles = vehicleService.getVehiclesByWebCategory(webCategory);

            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    vehicles,
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