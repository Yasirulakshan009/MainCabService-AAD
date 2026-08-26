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
public class CustomerDTO {

    private long customerID;

    @NotBlank(message = "Customer name cannot be blank!")
    private String customerName;

    @NotBlank(message = "Customer number cannot be blank!")
    private String customerNumber;

    @NotBlank(message = "Customer NIC cannot be blank!")
    private String customerNIC;

    @NotBlank(message = "Customer license number cannot be blank!")
    private String customerLicenseNumber;

    @NotBlank(message = "Customer address cannot be blank!")
    private String customerAddress;

    @NotNull(message = "Customer register date cannot be null!")
    private LocalDate customerRegisterDate;
}
