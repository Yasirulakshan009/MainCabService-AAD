package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.BookingCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingCustomerRepository extends JpaRepository<BookingCustomer,Long> {

    @Query("SELECT b FROM BookingCustomer b WHERE " +
            "LOWER(CAST(b.customerID AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.emailAddress) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BookingCustomer> searchBookingCustomers(@Param("keyword") String keyword);
}
