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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional

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
        log.info("Fetching all payments");
        try {
            List<Payment> payments = paymentRepository.findAll();
            List<PaymentDTO> paymentDTOs = new ArrayList<>();

            for (Payment payment : payments) {
                PaymentDTO dto = new PaymentDTO();
                dto.setPaymentID(payment.getPaymentID());
                dto.setAmount(payment.getAmount());
                dto.setStatus(payment.getPaymentStatus());
                dto.setDate(payment.getPaymentDate());

                if (payment.getRental() != null) {
                    dto.setRentalID(payment.getRental().getRentalID());
                }
                if (payment.getPaymentMethod() != null) {
                    dto.setPaymentMethod(payment.getPaymentMethod().getPaymentMethod());
                }

                paymentDTOs.add(dto);
            }
            return paymentDTOs;

        } catch (Exception e) {
            log.error("Error in getAllPayments(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {

        log.info("Fetching payments by status: " + status);

        try {
            List<Payment> payments = paymentRepository.findAll();
            List<PaymentDTO> paymentDTOs = new ArrayList<>();

            for (Payment payment : payments) {
                if (payment.getPaymentStatus() == status) {
                    PaymentDTO dto = new PaymentDTO();
                    dto.setPaymentID(payment.getPaymentID());
                    dto.setAmount(payment.getAmount());
                    dto.setStatus(payment.getPaymentStatus());
                    dto.setDate(payment.getPaymentDate());

                    if (payment.getRental() != null) {
                        dto.setRentalID(payment.getRental().getRentalID());
                    }
                    if (payment.getPaymentMethod() != null) {
                        dto.setPaymentMethod(payment.getPaymentMethod().getPaymentMethod());
                    }
                    paymentDTOs.add(dto);
                }
            }
            return paymentDTOs;

        } catch (Exception e) {
            log.error("Error in getPaymentsByStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PaymentDTO> searchPayments(String keyword) {
        log.info("Searching payments with keyword: " + keyword);
        try {
            List<Payment> payments = paymentRepository.findAll();
            List<PaymentDTO> paymentDTOs = new ArrayList<>();

            for (Payment payment : payments) {
                try {
                    Long searchId = Long.parseLong(keyword);

                    if (payment.getRental() != null && payment.getRental().getRentalID() == searchId) {
                        PaymentDTO dto = new PaymentDTO();
                        dto.setPaymentID(payment.getPaymentID());
                        dto.setAmount(payment.getAmount());
                        dto.setStatus(payment.getPaymentStatus());
                        dto.setDate(payment.getPaymentDate());
                        dto.setRentalID(payment.getRental().getRentalID());

                        if (payment.getPaymentMethod() != null) {
                            dto.setPaymentMethod(payment.getPaymentMethod().getPaymentMethod());
                        }
                        paymentDTOs.add(dto);
                    }
                } catch (NumberFormatException e) {
                    log.warn("Keyword is not a valid number: " + keyword);
                }
            }
            return paymentDTOs;

        } catch (Exception e) {
            log.error("Error in searchPayments(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getTotalRevenue() {
        log.info("Calculating total revenue");
        try {
            List<Payment> payments = paymentRepository.findAll();
            double totalRevenue = 0.0;

            for (Payment payment : payments) {
                if (payment.getPaymentStatus() == PaymentStatus.ALL_COMPLETED) {
                    totalRevenue += payment.getAmount();
                }
            }
            return totalRevenue;

        } catch (Exception e) {
            log.error("Error in getTotalRevenue(): " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public double getMonthlyRevenue() {
        log.info("Calculating monthly revenue");
        try {
            List<Payment> payments = paymentRepository.findAll();
            double monthlyRevenue = 0.0;
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            for (Payment payment : payments) {
                if (payment.getPaymentStatus() == PaymentStatus.ALL_COMPLETED && payment.getPaymentDate() != null) {
                    if (payment.getPaymentDate().getMonthValue() == currentMonth && payment.getPaymentDate().getYear() == currentYear) {
                        monthlyRevenue += payment.getAmount();
                    }
                }
            }
            return monthlyRevenue;

        } catch (Exception e) {
            log.error("Error in getMonthlyRevenue(): " + e.getMessage());
            return 0.0;
        }
    }

}
