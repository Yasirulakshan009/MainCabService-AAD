package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.ACType;
import lk.ijse.MainCabService.enumeratios.Category;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.enumeratios.VehicleTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long vehicleID;

    private String  vehicleModel;
    private String plateNO;
    private String licenseNO;
    private String insuranceNO;
    private double dailyRate;
    private int seats;
    private int bags;

    @Enumerated(EnumType.STRING)
    private VehicleTag vehicleTag;

    @Enumerated(EnumType.STRING)
    private ACType acType;

    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    @OneToMany(mappedBy = "vehicles")
    private List<Rental> rentals;

    @OneToMany(mappedBy = "vehicle")
    private List<Maintenance> maintenanceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_category_id")
    private VehicleCategory vehicleCategory;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] vehicleImage;

}
