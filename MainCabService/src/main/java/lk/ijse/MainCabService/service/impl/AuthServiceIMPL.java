package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.*;
import lk.ijse.MainCabService.entity.User;
import lk.ijse.MainCabService.entity.UserRole;
import lk.ijse.MainCabService.enumeratios.DashboardSection;
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
import java.util.Objects;

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

            UserRole role = userRoleRepository.findById(
                    userDTO.getUserRole().getUserRoleID()
            ).orElseThrow(() ->
                    new RuntimeException("User role not found!")
            );
            user.setUserRole(role);

        } else {

            UserRole customerRole = userRoleRepository.findByRole(Role.CUSTOMER)
                    .orElseThrow(() ->
                            new RuntimeException("Default CUSTOMER role not found!")
                    );
            user.setUserRole(customerRole);
        }

        user.setPermissions(userDTO.getPermissions());

        userRepository.save(user);

    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
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

        String token = jwtUtil.generateToken(userDTO);

        String roleName = user.getUserRole() != null ? user.getUserRole().getRole().name() : "STAFF";

        List<DashboardSection> permissions = user.getPermissions();

        return new AuthResponseDTO(token, roleName, permissions);
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
    public void updateUserStatus(Long id, UserStatus status) {
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        User loggedInUser = userRepository.findByUserEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        if (loggedInUser.getUserID() == id && status == UserStatus.INACTIVE) {
            throw new RuntimeException("Action Denied: You cannot deactivate your own currently logged-in account!");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public void updateNameAndPhone(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUserName(userDTO.getUserName());
        user.setPhone(userDTO.getPhone());
        user.setPermissions(userDTO.getPermissions());

        userRepository.save(user);
    }

    @Override
    public void changeEmail(ChangeEmailDTO changeEmailDTO) {
        if (!changeEmailDTO.getNewEmail().equals(changeEmailDTO.getConfirmNewEmail())) {
            throw new RuntimeException("New emails do not match!");
        }

        User user = userRepository.findByUserEmail(changeEmailDTO.getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + changeEmailDTO.getCurrentEmail()));

        if (userRepository.existsByUserEmail(changeEmailDTO.getNewEmail())) {
            throw new RuntimeException("New email is already in use by another account!");
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
