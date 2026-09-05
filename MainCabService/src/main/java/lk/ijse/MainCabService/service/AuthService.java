package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.dto.ChangeEmailDTO;
import lk.ijse.MainCabService.dto.ChangePasswordDTO;
import lk.ijse.MainCabService.dto.UserDTO;
import lk.ijse.MainCabService.enumeratios.UserStatus;

import java.util.List;

public interface AuthService {

    void register(UserDTO userDTO);
    String authenticate(AuthRequestDTO authRequestDTO);
    List<UserDTO> getAllUsers();
    List<UserDTO> getCustomersOnly();
    void updateUserStatus(Long id, UserStatus status);
    void updateNameAndPhone(Long id, UserDTO userDTO);
    void changeEmail(ChangeEmailDTO changeEmailDTO);
    void changePassword(ChangePasswordDTO changePasswordDTO);
}
