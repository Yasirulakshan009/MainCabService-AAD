package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customerID;

    private String customerName;
    private String customerNumber;
    private String customerNIC;
    private String customerLicenseNumber;
    private String customerAddress;
    private LocalDate customerRegisterDate;

    @OneToMany(mappedBy = "customer")
    private List<Rental> rentalList;
}
