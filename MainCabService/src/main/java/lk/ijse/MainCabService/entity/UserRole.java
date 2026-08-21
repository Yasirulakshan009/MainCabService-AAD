package lk.ijse.MainCabService.entity;


import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userRoleID;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "userRole")
    private List<User> userList;
}
