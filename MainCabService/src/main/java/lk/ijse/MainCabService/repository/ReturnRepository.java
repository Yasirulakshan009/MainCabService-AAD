package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Return;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return,Long> {

    List<Return> findByReturnStatus(ReturnStatus returnStatus);


    List<Return> findByRental_RentalID(Long rentalID);

    long countByReturnStatus(ReturnStatus returnStatus);
}
