package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query("SELECT b FROM Booking b LEFT JOIN b.bookingCustomer c WHERE " +
            "LOWER(CAST(b.bookingID AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.bookingCustomerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Booking> searchBookings(@Param("keyword") String keyword);
}
