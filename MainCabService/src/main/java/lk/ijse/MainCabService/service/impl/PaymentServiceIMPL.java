package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.enumeratios.PaymentStatus;
import lk.ijse.MainCabService.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceIMPL implements PaymentService {
    @Override
    public void savePayment(PaymentDTO paymentDTO) {

    }

    @Override
    public void updatePayment(PaymentDTO paymentDTO) {

    }

    @Override
    public void deletePayment(Long id) {

    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return List.of();
    }

    @Override
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {
        return List.of();
    }

    @Override
    public List<PaymentDTO> searchPayments(String keyword) {
        return List.of();
    }

    @Override
    public double getTotalRevenue() {
        return 0;
    }

    @Override
    public double getPendingAmount() {
        return 0;
    }

    @Override
    public double getMonthlyRevenue() {
        return 0;
    }
}
