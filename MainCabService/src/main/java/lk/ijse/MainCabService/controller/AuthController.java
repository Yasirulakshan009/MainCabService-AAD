package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.dto.ChangeEmailDTO;
import lk.ijse.MainCabService.dto.ChangePasswordDTO;
import lk.ijse.MainCabService.dto.UserDTO;
import lk.ijse.MainCabService.enumeratios.UserStatus;
import lk.ijse.MainCabService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> register(@RequestBody UserDTO userDTO) {
        try {
            authService.register(userDTO);
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

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> login(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            String token = authService.authenticate(authRequestDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    token,
                    ResponseMessage.SUCCESS_MESSAGE
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    "Invalid Credentials or " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllUsers() {
        try {
            List<UserDTO> userList = authService.getAllUsers();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    userList,
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

    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllCustomers() {
        try {
            List<UserDTO> customerList = authService.getCustomersOnly();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    customerList,
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

    @PutMapping(value = "/customer-status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateCustomerStatus(@PathVariable Long id, @RequestParam UserStatus status) {
        try {
            authService.updateCustomerStatus(id, status);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    "Customer status updated successfully!"
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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = authService.getUserById(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    userDTO,
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

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            authService.updateUser(id, userDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    "User updated successfully!"
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

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    "User deleted successfully!"
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

    @PutMapping(value = "/change-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> changeEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
        try {
            authService.changeEmail(changeEmailDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    "Email change successfully!"
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updatePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            authService.changePassword(changePasswordDTO);
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    null,
                    "Password updated successfully!"
            );
            return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        } catch (Exception e) {
            CommonResponse errorResponse = new CommonResponse(
                    ResponseCode.OPERATION_FAILED,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
