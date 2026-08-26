package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.enumeratios.ACType;
import lk.ijse.MainCabService.enumeratios.Category;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.enumeratios.VehicleTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private long vehicleID;

    @NotBlank(message = "Vehicle name cannot be blank!")
    private String vehicleName;

    @NotNull(message = "Vehicle category cannot be null!")
    private Category vehicleCategory;

    @NotBlank(message = "Plate number cannot be blank!")
    private String plateNumber;

    @NotNull(message = "Daily price cannot be null!")
    @Min(value = 0, message = "Daily price must be greater than or equal to 0!")
    private Double dailyPrice;

    @NotBlank(message = "Insurance number cannot be blank!")
    private String insuranceNo;

    @NotBlank(message = "License number cannot be blank!")
    private String LicenseNo;

    @NotNull(message = "Seats cannot be null!")
    @Min(value = 1, message = "Seats must be at least 1!")
    private Integer seats;

    @NotNull(message = "Bags cannot be null!")
    @Min(value = 0, message = "Bags cannot be negative!")
    private Integer bags;

    @NotNull(message = "Vehicle status cannot be null!")
    private VehicleStatus status;

    @NotNull(message = "Vehicle tag cannot be null!")
    private VehicleTag tagClass;

    @NotNull(message = "AC type cannot be null!")
    private ACType acType;

    private Object vehicleImage;
}
