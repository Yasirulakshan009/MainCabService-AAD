package lk.ijse.MainCabService.dto;

import jakarta.persistence.*;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import lk.ijse.MainCabService.enumeratios.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceDTO {

    private long maintenanceID;
    private long vehicleID;
    private String title;
    private String description;
    private Priority priority;
    private MaintenanceStatus maintenanceStatus;
    private LocalDate scheduledDate;
    private double cost;
    private String vendor;

}
