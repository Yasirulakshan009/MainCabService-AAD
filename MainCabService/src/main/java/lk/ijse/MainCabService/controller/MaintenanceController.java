package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.MaintenanceDTO;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import lk.ijse.MainCabService.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        try {
            maintenanceService.saveMaintenance(maintenanceDTO);
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
    public ResponseEntity<CommonResponse> updateMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        try {
            maintenanceService.updateMaintenance(maintenanceDTO);
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
    public ResponseEntity<CommonResponse> deleteMaintenance(@PathVariable Long id) {
        try {
            maintenanceService.deleteMaintenance(id);
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
    public ResponseEntity<CommonResponse> getMaintenanceById(@PathVariable Long id) {
        try {
            MaintenanceDTO maintenanceDTO = maintenanceService.getMaintenanceById(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    maintenanceDTO,
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
    public ResponseEntity<CommonResponse> getAllMaintenance() {
        try {
            List<MaintenanceDTO> maintenanceDTOList = maintenanceService.getAllMaintenance();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    maintenanceDTOList,
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

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMaintenanceByStatus(@PathVariable MaintenanceStatus status) {
        try {
            List<MaintenanceDTO> maintenanceDTOList = maintenanceService.getMaintenanceByStatus(status);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    maintenanceDTOList,
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