package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);

    @Query("SELECT v FROM Vehicle v LEFT JOIN v.vehicleCategory vc WHERE " +
            "LOWER(v.vehicleModel) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(v.plateNO) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(CAST(vc.vehicleCategory AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Vehicle> searchVehicles(@Param("keyword") String keyword);

    long countByVehicleStatus(VehicleStatus vehicleStatus);

    List<Vehicle> findByShowOnWebsiteTrue();
}
