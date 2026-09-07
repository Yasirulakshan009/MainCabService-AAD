package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.*;
import lk.ijse.MainCabService.enumeratios.UserStatus;

import java.util.List;

public interface AuthService {

    void register(UserDTO userDTO);
    AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO);
    List<UserDTO> getAllUsers();
    List<UserDTO> getCustomersOnly();
    void updateUserStatus(Long id, UserStatus status);
    void updateNameAndPhone(Long id, UserDTO userDTO);
    void changeEmail(ChangeEmailDTO changeEmailDTO);
    void changePassword(ChangePasswordDTO changePasswordDTO);
}
