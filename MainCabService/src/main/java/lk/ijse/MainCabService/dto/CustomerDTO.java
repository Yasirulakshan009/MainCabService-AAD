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
public class CustomerDTO {

    private Long customerID;

    @NotBlank(message = "Customer name cannot be blank!")
    private String customerName;

    @NotBlank(message = "Customer number cannot be blank!")
    @Pattern(regexp = "^07[0-9]{8}$",
            message = "Invalid Sri Lankan phone number"
    )
    private String customerNumber;

    @NotBlank(message = "Customer NIC cannot be blank!")
    @Pattern(regexp = "^([0-9]{9}[VvXx]|[0-9]{12})$",
            message = "Invalid Sri Lankan NIC number"
    )
    private String customerNIC;

    @NotBlank(message = "Customer license number cannot be blank!")
    @Pattern(regexp = "^([0-9]{9}[A-Za-z]|[0-9]{9})$",
            message = "Invalid driving license number"
    )
    private String customerLicenseNumber;

    @NotBlank(message = "Customer address cannot be blank!")
    private String customerAddress;

    @NotNull(message = "Customer register date cannot be null!")
    private LocalDate customerRegisterDate;
}
