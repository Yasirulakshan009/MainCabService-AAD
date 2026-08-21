package lk.ijse.MainCabService.dto;

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
    private String vehicleModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private String pickupAddress;
    private BookingStatus bookingStatus;
    private long bookingCustomerID;
    private String bookingCustomerName;
}
