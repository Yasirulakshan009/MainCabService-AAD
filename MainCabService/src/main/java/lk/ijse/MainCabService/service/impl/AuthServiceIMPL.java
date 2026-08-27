package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.dto.UserDTO;
import lk.ijse.MainCabService.entity.User;
import lk.ijse.MainCabService.repository.UserRepository;
import lk.ijse.MainCabService.security.JwtUtil;
import lk.ijse.MainCabService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String register(UserDTO userDTO) {
        return "";
    }

    @Override
    public String authenticate(AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );

        User user = userRepository.findByUserEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(user.getUserID());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserRole(user.getUserRole());

        return jwtUtil.generateToken(userDTO);
    }
}
