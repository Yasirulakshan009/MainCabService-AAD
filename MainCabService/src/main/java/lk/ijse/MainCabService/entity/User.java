package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.DashboardSection;
import lk.ijse.MainCabService.enumeratios.Role;
import lk.ijse.MainCabService.enumeratios.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    private String userName;

    private String userEmail;

    private String phone;
    
    private String userPassword;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<DashboardSection> permissions;



}
