package lk.ijse.MainCabService.dto;

import lk.ijse.MainCabService.entity.UserRole;
import lk.ijse.MainCabService.enumeratios.DashboardSection;
import lk.ijse.MainCabService.enumeratios.Role;
import lk.ijse.MainCabService.enumeratios.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private long userID;
    private String userName;
    private String userEmail;
    private String phone;
    private String userPassword;
    private UserStatus status;
    private UserRole userRole;
    private List<DashboardSection> permissions;
}
