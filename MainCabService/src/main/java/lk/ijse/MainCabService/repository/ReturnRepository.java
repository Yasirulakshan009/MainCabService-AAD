package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Return;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return,Long> {

    List<Return> findByReturnStatus(ReturnStatus returnStatus);


    @Query(" SELECT r FROM Return r WHERE r.returnID = :id OR r.rental.rentalID = :id ")
    List<Return> findByReturnIDOrRentalID(@Param("id") Long id);

    long countByReturnStatus(ReturnStatus returnStatus);
}
