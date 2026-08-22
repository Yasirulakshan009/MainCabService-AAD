package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.entity.Payment;
import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.PaymentStatus;
import lk.ijse.MainCabService.repository.PaymentMethodRepository;
import lk.ijse.MainCabService.repository.PaymentRepository;
import lk.ijse.MainCabService.repository.RentalRepository;
import lk.ijse.MainCabService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class PaymentServiceIMPL implements PaymentService {

    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void savePayment(PaymentDTO paymentDTO) {

        log.info("Executing savePayment()");
        try {
            Payment payment = new Payment();

            Rental rental = rentalRepository.findById(paymentDTO.getRentalID())
                    .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + paymentDTO.getRentalID()));

            payment.setRental(rental);

            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentStatus(paymentDTO.getStatus());
            payment.setPaymentDate(LocalDate.now());

            Method methodEnum = paymentDTO.getPaymentMethod();
            if (methodEnum != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(methodEnum);
                if (paymentMethod != null) {
                    payment.setPaymentMethod(paymentMethod);
                }
            }

            paymentRepository.save(payment);
            log.info("Payment saved successfully for Rental ID: " + paymentDTO.getRentalID());

        } catch (Exception e) {
            log.error("Error in savePayment(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePayment(PaymentDTO paymentDTO) {

        log.info("Executing updatePayment() for Rental ID: " + paymentDTO.getRentalID());
        try {

            Payment payment = paymentRepository.findByRental_RentalID(paymentDTO.getRentalID())
                    .orElse(new Payment());

            Rental rental = rentalRepository.findById(paymentDTO.getRentalID())
                    .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + paymentDTO.getRentalID()));
            payment.setRental(rental);

            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentStatus(paymentDTO.getStatus());

            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(LocalDate.now());
            }

            Method methodEnum = paymentDTO.getPaymentMethod();
            if (methodEnum != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(methodEnum);
                if (paymentMethod != null) {
                    payment.setPaymentMethod(paymentMethod);
                }
            }

            paymentRepository.save(payment);
            log.info("Payment updated successfully!");

        } catch (Exception e) {
            log.error("Error in updatePayment(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePayment(Long id) {

        log.info("Deleting payment for Rental ID: " + id);
        try {
            paymentRepository.deleteByRental_RentalID(id);
            log.info("Payment deleted successfully!");
        } catch (Exception e) {
            log.error("Error in deletePayment(): " + e.getMessage());
            throw new RuntimeException(e);
        }
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
    public double getMonthlyRevenue() {
        return 0;
    }
}
