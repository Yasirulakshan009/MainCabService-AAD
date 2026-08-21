package lk.ijse.MainCabService.dto;

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
    private LocalDate bookingCustomerRegisterDate;
    private String bookingCustomerName;
    private String bookingCustomerEmail;
    private String bookingCustomerNumber;
    private String bookingCustomerLicenseNumber;
}
