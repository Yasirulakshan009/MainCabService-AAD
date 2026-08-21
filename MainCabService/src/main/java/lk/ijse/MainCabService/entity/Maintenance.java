package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import lk.ijse.MainCabService.enumeratios.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maintenanceID;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;

    private LocalDate scheduledDate;
    private double cost;
    private String vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
