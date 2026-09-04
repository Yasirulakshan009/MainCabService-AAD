package lk.ijse.MainCabService.controller;

import jakarta.validation.Valid;
import lk.ijse.MainCabService.constants.CommonResponse;
import lk.ijse.MainCabService.constants.ResponseCode;
import lk.ijse.MainCabService.constants.ResponseMessage;
import lk.ijse.MainCabService.dto.WebsiteSettingDTO;
import lk.ijse.MainCabService.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/website-settings")
@CrossOrigin
public class WebsiteSettingController {

    private final WebsiteService websiteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getWebsiteSettings() {
        try {
            WebsiteSettingDTO dto = websiteService.getWebsiteSettings();
            CommonResponse commonResponse = new CommonResponse(
                    ResponseCode.OPERATION_SUCCESS,
                    dto,
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

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateWebsiteSettings(@Valid @RequestBody WebsiteSettingDTO websiteSettingDTO) {
        try {
            websiteService.updateWebsiteSettings(websiteSettingDTO);
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