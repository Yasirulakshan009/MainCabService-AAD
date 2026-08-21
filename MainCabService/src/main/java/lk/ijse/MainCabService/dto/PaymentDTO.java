package lk.ijse.MainCabService.dto;

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
    private long rentalID;
    private double amount;
    private Method paymentMethod;
    private LocalDate date;
    private PaymentStatus status;

}
