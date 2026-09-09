package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.enumeratios.WebCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);

    @Query(value = "SELECT v.* FROM vehicles v LEFT JOIN vehicle_category vc ON v.vehicle_category_id = vc.vehicle_category_id " +
            "WHERE LOWER(v.vehicle_model) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.plateno) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(v.vehicleid AS CHAR)) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Vehicle> searchVehicles(@Param("keyword") String keyword);

    long countByVehicleStatus(VehicleStatus vehicleStatus);

    List<Vehicle> findByShowOnWebsiteTrue();

    List<Vehicle> findByShowOnWebsiteTrueAndWebCategory(WebCategory webCategory);
}
