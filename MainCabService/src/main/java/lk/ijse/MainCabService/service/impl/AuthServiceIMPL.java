package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.dto.ChangeEmailDTO;
import lk.ijse.MainCabService.dto.ChangePasswordDTO;
import lk.ijse.MainCabService.dto.UserDTO;
import lk.ijse.MainCabService.entity.User;
import lk.ijse.MainCabService.entity.UserRole;
import lk.ijse.MainCabService.enumeratios.Role;
import lk.ijse.MainCabService.enumeratios.UserStatus;
import lk.ijse.MainCabService.repository.UserRepository;
import lk.ijse.MainCabService.repository.UserRoleRepository;
import lk.ijse.MainCabService.security.JwtUtil;
import lk.ijse.MainCabService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
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
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : UserStatus.ACTIVE);

        if (userDTO.getUserRole() != null) {
            user.setUserRole(userDTO.getUserRole());
        } else {
            UserRole customerRole = userRoleRepository.findByRole(Role.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Default CUSTOMER role not found!"));
            user.setUserRole(customerRole);
        }

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

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Your account is inactive. Please contact the administrator.");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserID(user.getUserID());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserRole(user.getUserRole());

        return jwtUtil.generateToken(userDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByUserRole_RoleNot(Role.CUSTOMER);
        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setUserID(user.getUserID());
            dto.setUserName(user.getUserName());
            dto.setUserEmail(user.getUserEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setUserRole(user.getUserRole());
            dto.setPermissions(user.getPermissions());

            userDTOList.add(dto);
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> getCustomersOnly() {
        List<User> customers = userRepository.findByUserRole_Role(Role.CUSTOMER);
        List<UserDTO> customerDTOList = new ArrayList<>();

        for (User user : customers) {
            UserDTO dto = new UserDTO();
            dto.setUserID(user.getUserID());
            dto.setUserName(user.getUserName());
            dto.setUserEmail(user.getUserEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setUserRole(user.getUserRole());

            customerDTOList.add(dto);
        }
        return customerDTOList;
    }

    @Override
    public void updateCustomerStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        if (user.getUserRole().getRole() != Role.CUSTOMER) {
            throw new RuntimeException("User is not a customer!");
        }

        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserDTO dto = new UserDTO();
        dto.setUserID(user.getUserID());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setUserRole(user.getUserRole());
        dto.setPermissions(user.getPermissions());

        return dto;
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUserName(userDTO.getUserName());
        user.setUserEmail(userDTO.getUserEmail());
        user.setPhone(userDTO.getPhone());
        user.setStatus(userDTO.getStatus());
        user.setUserRole(userDTO.getUserRole());
        user.setPermissions(userDTO.getPermissions());

        if (userDTO.getUserPassword() != null && !userDTO.getUserPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void changeEmail(ChangeEmailDTO changeEmailDTO) {
        if (!changeEmailDTO.getNewEmail().equals(changeEmailDTO.getConfirmNewEmail())) {
            throw new RuntimeException("New emails do not match!");
        }

        User user = userRepository.findByUserEmail(changeEmailDTO.getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("Current user not found!"));

        if (userRepository.existsByUserEmail(changeEmailDTO.getNewEmail())) {
            throw new RuntimeException("New email is already in use!");
        }

        user.setUserEmail(changeEmailDTO.getNewEmail());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new RuntimeException("New passwords do not match!");
        }

        User user = userRepository.findByUserEmail(changePasswordDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getUserPassword())) {
            throw new RuntimeException("Incorrect current password!");
        }

        user.setUserPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }
}
