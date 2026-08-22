package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.enumeratios.PaymentStatus;

import java.util.List;

public interface PaymentService {

    void savePayment(PaymentDTO paymentDTO);

    void updatePayment(PaymentDTO paymentDTO);

    void deletePayment(Long id);

    List<PaymentDTO> getAllPayments();

    List<PaymentDTO> getPaymentsByStatus(PaymentStatus status);

    List<PaymentDTO> searchPayments(String keyword);

    double getTotalRevenue();

    double getMonthlyRevenue();
}
