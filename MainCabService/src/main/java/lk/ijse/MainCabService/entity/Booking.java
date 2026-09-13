package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingID;

    private String bookingVehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime pickupTime;
    private LocalTime returnTime;
    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private LocalDate bookingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_customer_id")
    private BookingCustomer bookingCustomer;
}
