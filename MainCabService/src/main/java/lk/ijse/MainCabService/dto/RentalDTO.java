package lk.ijse.MainCabService.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    private long rentalID;

    @NotNull(message = "Start date cannot be null!")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null!")
    private LocalDate endDate;

    private String pickupAddress;

    private Double deliveryFee;

    @NotNull(message = "Total amount cannot be null!")
    @Min(value = 0, message = "Total amount must be greater than or equal to 0!")
    private Double totalAmount;

    @NotNull(message = "Payment method cannot be null!")
    private Method paymentMethod;

    @NotNull(message = "Rental status cannot be null!")
    private RentalStatus rentalStatus;

    @NotNull(message = "Customer ID cannot be null!")
    private Long customerID;

    @NotNull(message = "Vehicle ID cannot be null!")
    private Long vehicleID;


}
