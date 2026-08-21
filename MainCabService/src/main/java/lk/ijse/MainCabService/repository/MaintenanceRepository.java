package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Maintenance;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance,Long> {

    List<Maintenance> findByMaintenanceStatus(MaintenanceStatus maintenanceStatus);
}
