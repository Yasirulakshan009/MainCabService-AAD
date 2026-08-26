package lk.ijse.MainCabService.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Vehicle ID cannot be null!")
    private Long vehicleID;

    @NotBlank(message = "Maintenance title cannot be blank!")
    private String title;

    private String description;

    @NotNull(message = "Priority cannot be null!")
    private Priority priority;

    @NotNull(message = "Maintenance status cannot be null!")
    private MaintenanceStatus maintenanceStatus;

    @NotNull(message = "Scheduled date cannot be null!")
    private LocalDate scheduledDate;

    @NotNull(message = "Cost cannot be null!")
    @Min(value = 0, message = "Cost must be greater than or equal to 0!")
    private Double cost;

    @NotBlank(message = "Vendor cannot be blank!")
    private String vendor;

}
