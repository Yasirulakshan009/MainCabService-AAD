package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterDTO {

    @NotBlank(message = "Name cannot be blank!")
    private String userName;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email format!")
    private String userEmail;

    @NotBlank(message = "Phone number cannot be blank!")
    @Pattern(regexp = "^07[0-9]{8}$",
            message = "Invalid Sri Lankan phone number"
    )
    private String phone;

    @NotBlank(message = "Password cannot be blank!")
    private String userPassword;

    @NotBlank(message = "Confirm password cannot be blank!")
    private String confirmPassword;
}