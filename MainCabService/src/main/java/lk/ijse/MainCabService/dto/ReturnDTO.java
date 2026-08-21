package lk.ijse.MainCabService.dto;

import jakarta.persistence.*;
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

    private LocalDate returnDate;
    private LocalDate initialReturnDate;
    private String notes;
    private double extraCharges;
    private double finalAmount;
    private ReturnStatus returnStatus;
    private Method paymentMethod;

    private long rentalID;

}
