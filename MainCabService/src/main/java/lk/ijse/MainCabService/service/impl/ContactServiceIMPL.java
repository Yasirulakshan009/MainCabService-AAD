package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.ContactMessageDTO;
import lk.ijse.MainCabService.service.ContactService;
import lk.ijse.MainCabService.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceIMPL implements ContactService {

    private final EmailService emailService;

    @Override
    public void sendContactMessage(ContactMessageDTO contactMessageDTO) {

        log.info("Executing sendContactMessage()");

        try {
            emailService.sendContactMessageEmail(contactMessageDTO);
            log.info("Contact message email sent successfully!");

        } catch (Exception e) {
            log.error("Error in sendContactMessage(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}