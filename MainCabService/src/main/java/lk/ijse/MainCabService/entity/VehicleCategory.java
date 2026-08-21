package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vehicle_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long vehicleCategoryId;

    @Enumerated(EnumType.STRING)
    private Category vehicleCategory;

    @OneToMany(mappedBy = "vehicleCategory")
    private List<Vehicle> vehicleList;

}
