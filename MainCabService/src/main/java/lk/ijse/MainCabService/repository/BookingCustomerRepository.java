package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.BookingCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingCustomerRepository extends JpaRepository<BookingCustomer,Long> {

    @Query(value = "SELECT * FROM booking_customers_info b WHERE " +
            "LOWER(CAST(b.booking_customerid AS CHAR)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.booking_customer_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.booking_customer_email) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<BookingCustomer> searchBookingCustomers(@Param("keyword") String keyword);
}
