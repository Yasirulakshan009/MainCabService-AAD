package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.enumeratios.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private long bookingID;

    @NotBlank(message = "Vehicle model cannot be blank!")
    private String vehicleModel;

    @NotNull(message = "Start date cannot be null!")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null!")
    private LocalDate endDate;

    private String pickupAddress;

    @NotNull(message = "Booking status cannot be null!")
    private BookingStatus bookingStatus;

    @NotNull(message = "Booking customer ID cannot be null!")
    private Long bookingCustomerID;

    @NotBlank(message = "Customer name cannot be blank!")
    private String bookingCustomerName;
}
