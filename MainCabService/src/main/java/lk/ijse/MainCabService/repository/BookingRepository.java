package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query(value = "SELECT * FROM bookings b LEFT JOIN booking_customers_info c ON b.booking_customer_id = c.booking_customerid WHERE " +
            "LOWER(CAST(b.bookingid AS CHAR)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.booking_customer_name) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Booking> searchBookings(@Param("keyword") String keyword);
}
