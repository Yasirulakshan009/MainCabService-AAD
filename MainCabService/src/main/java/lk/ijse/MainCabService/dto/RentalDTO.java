package lk.ijse.MainCabService.dto;


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

    private LocalDate startDate;
    private LocalDate endDate;
    private String pickupAddress;
    private double deliveryFee;
    private double totalAmount;
    private Method paymentMethod;
    private RentalStatus rentalStatus;

    private long customerID;
    private long vehicleID;


}
