package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.User;
import lk.ijse.MainCabService.enumeratios.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    List<User> findByUserRole_RoleNot(Role role);

    List<User> findByUserRole_Role(Role role);
}
