package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    private Long bookingCustomerID;

    @NotNull(message = "Register date cannot be null!")
    private LocalDate bookingCustomerRegisterDate;

    @NotBlank(message = "Customer name cannot be blank!")
    private String bookingCustomerName;

    @NotBlank(message = "Customer email cannot be blank!")
    private String bookingCustomerEmail;

    @NotBlank(message = "Customer number cannot be blank!")
    @Pattern(regexp = "^07[0-9]{8}$",
            message = "Invalid Sri Lankan phone number"
    )
    private String bookingCustomerNumber;

    @NotBlank(message = "Customer license number cannot be blank!")
    @Pattern(regexp = "^([0-9]{9}[A-Za-z]|[0-9]{9})$",
            message = "Invalid driving license number"
    )
    private String bookingCustomerLicenseNumber;
}
