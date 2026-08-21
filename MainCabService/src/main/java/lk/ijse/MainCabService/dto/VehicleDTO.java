package lk.ijse.MainCabService.dto;

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
    private String vehicleName;
    private Category vehicleCategory;
    private String plateNumber;
    private double dailyPrice;
    private String insuranceNo;
    private String LicenseNo;
    private int seats;
    private int bags;
    private VehicleStatus status;
    private VehicleTag tagClass;
    private ACType acType;

    private Object vehicleImage;
}
