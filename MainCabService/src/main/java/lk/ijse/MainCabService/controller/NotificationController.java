package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.NotificationDTO;
import lk.ijse.MainCabService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/notifications")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllNotifications() {
        try {
            List<NotificationDTO> list = notificationService.getAllNotifications();
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_SUCCESS, list, ResponseMessage.SUCCESS_MESSAGE),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_FAILED, null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/unread-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getUnreadCount() {
        try {
            long count = notificationService.getUnreadCount();
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_SUCCESS, Map.of("count", count), ResponseMessage.SUCCESS_MESSAGE),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_FAILED, null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(value = "/{id}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> markAsRead(@PathVariable Long id) {
        try {
            notificationService.markAsRead(id);
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_SUCCESS, null, ResponseMessage.UPDATE_SUCCESS),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_FAILED, null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(value = "/mark-all-read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> markAllAsRead() {
        try {
            notificationService.markAllAsRead();
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_SUCCESS, null, ResponseMessage.UPDATE_SUCCESS),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new CommonResponse(ResponseCode.OPERATION_FAILED, null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}