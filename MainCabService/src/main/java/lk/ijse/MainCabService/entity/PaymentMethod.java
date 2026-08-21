package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentMethodID;

    @Enumerated(EnumType.STRING)
    private Method paymentMethod;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Rental> rentalList;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Return> returnList;
}
