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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserDTO userDTO) {

        if (!userDTO.getUserPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }

        if (userRepository.existsByUserEmail(userDTO.getUserEmail())) {
            throw new RuntimeException("User already exists with this email!");
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setUserEmail(userDTO.getUserEmail());
        user.setPhone(userDTO.getPhone());

        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));

        user.setStatus(userDTO.getStatus());
        user.setUserRole(userDTO.getUserRole());
        user.setPermissions(userDTO.getPermissions());

        userRepository.save(user);

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

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return null;
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}
