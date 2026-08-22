package lk.ijse.MainCabService.controller;


import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PaymentDTO> searchPayments(@PathVariable String id) {
        return paymentService.searchPayments(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping(value = "/total-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public double getTotalRevenue() {
        return paymentService.getTotalRevenue();
    }

    @GetMapping(value = "/monthly-revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public double getMonthlyRevenue() {
        return paymentService.getMonthlyRevenue();
    }
}
