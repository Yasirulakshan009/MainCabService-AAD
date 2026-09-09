package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailDTO {

    @NotBlank(message = "Current email cannot be blank!")
    private String currentEmail;

    @NotBlank(message = "New email cannot be blank!")
    private String newEmail;

    @NotBlank(message = "Confirm new email cannot be blank!")
    private String confirmNewEmail;
}
