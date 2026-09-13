package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.ContactMessageDTO;

public interface ContactService {

    void sendContactMessage(ContactMessageDTO contactMessageDTO);
}