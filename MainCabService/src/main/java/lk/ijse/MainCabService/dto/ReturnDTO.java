package lk.ijse.MainCabService.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDTO {

    private long returnID;

    @NotNull(message = "Return date cannot be null!")
    private LocalDate returnDate;

    @NotNull(message = "Initial return date cannot be null!")
    private LocalDate initialReturnDate;

    private String notes;

    private Double extraCharges;

    @NotNull(message = "Final amount cannot be null!")
    @Min(value = 0, message = "Final amount must be greater than or equal to 0!")
    private Double finalAmount;

    @NotNull(message = "Return status cannot be null!")
    private ReturnStatus returnStatus;

    private Method paymentMethod;

    @NotNull(message = "Rental ID cannot be null!")
    private Long rentalID;

}
