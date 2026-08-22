package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByRental_RentalID(Long rentalID);

    void deleteByRental_RentalID(Long rentalID);
}
