package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private long paymentID;

    @NotNull(message = "Rental ID cannot be null!")
    private Long rentalID;

    @NotNull(message = "Amount cannot be null!")
    @Min(value = 0, message = "Amount must be greater than or equal to 0!")
    private Double amount;

    @NotNull(message = "Payment method cannot be null!")
    private Method paymentMethod;

    @NotNull(message = "Payment date cannot be null!")
    private LocalDate date;

    @NotNull(message = "Payment status cannot be null!")
    private PaymentStatus status;

}
