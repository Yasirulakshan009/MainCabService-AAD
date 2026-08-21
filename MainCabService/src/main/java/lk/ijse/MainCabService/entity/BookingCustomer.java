package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "booking_customers_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingCustomerID;

    private LocalDate bookingCustomerRegisterDate;
    private String bookingCustomerName;
    private String bookingCustomerEmail;
    private String bookingCustomerNumber;
    private String bookingCustomerLicenseNumber;


    @OneToMany(mappedBy = "bookingCustomer")
    private List<Booking> bookingList;

}
