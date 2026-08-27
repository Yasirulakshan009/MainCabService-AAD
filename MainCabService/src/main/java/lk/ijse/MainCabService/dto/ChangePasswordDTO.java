package lk.ijse.MainCabService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
