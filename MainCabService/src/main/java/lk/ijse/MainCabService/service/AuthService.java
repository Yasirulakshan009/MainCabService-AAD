package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.dto.UserDTO;

public interface AuthService {

    String register(UserDTO userDTO);

    String authenticate(AuthRequestDTO authRequestDTO);
}
