package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCustomerDTO {

    private long bookingCustomerID;

    @NotNull(message = "Register date cannot be null!")
    private LocalDate bookingCustomerRegisterDate;

    @NotBlank(message = "Customer name cannot be blank!")
    private String bookingCustomerName;

    @NotBlank(message = "Customer email cannot be blank!")
    private String bookingCustomerEmail;

    @NotBlank(message = "Customer number cannot be blank!")
    private String bookingCustomerNumber;

    @NotBlank(message = "Customer license number cannot be blank!")
    private String bookingCustomerLicenseNumber;
}
