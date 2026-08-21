package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.enumeratios.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory,Long> {

    VehicleCategory findByVehicleCategory(Category vehicleCategory);
}
