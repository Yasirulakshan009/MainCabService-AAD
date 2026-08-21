package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.enumeratios.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {

    List<Rental> findByRentalStatus(RentalStatus rentalStatus);


    long countByRentalStatus(RentalStatus rentalStatus);


    @Query("SELECT r FROM Rental r WHERE r.rentalID = :id OR r.vehicles.vehicleID = :id")
    List<Rental> findByRentalIDOrVehicleID(@Param("id") Long id);
}
