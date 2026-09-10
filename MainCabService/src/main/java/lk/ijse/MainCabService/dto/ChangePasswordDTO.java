package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Current password cannot be blank!")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank!")
    private String newPassword;

    @NotBlank(message = "Confirm new password cannot be blank!")
    private String confirmNewPassword;
}
