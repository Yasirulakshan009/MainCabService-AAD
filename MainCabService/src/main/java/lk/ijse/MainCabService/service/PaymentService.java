package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.PaymentDTO;

public interface PaymentService {

    void savePayment(PaymentDTO paymentDTO);

    void updatePayment(PaymentDTO paymentDTO);
}
