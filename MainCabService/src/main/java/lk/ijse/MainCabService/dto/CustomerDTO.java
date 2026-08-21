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
public class CustomerDTO {

    private long customerID;

    private String customerName;
    private String customerNumber;
    private String customerNIC;
    private String customerLicenseNumber;
    private String customerAddress;
    private LocalDate customerRegisterDate;
}
