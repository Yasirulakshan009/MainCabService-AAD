package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.VehicleDTO;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;

import java.util.List;

public interface VehicleService {

    void saveVehicle(VehicleDTO vehicleDTO);

    void updateVehicle(VehicleDTO vehicleDTO);

    void deleteVehicle(Long id);

    VehicleDTO getVehicleById(Long id);

    List<VehicleDTO> getAllVehicles();

    List<VehicleDTO> getVehiclesByStatus(VehicleStatus status);

    List<VehicleDTO> searchVehicles(String keyword);

    long getVehicleCountByStatus(VehicleStatus status);

    long getTotalVehicleCount();

    List<VehicleDTO> getWebsiteVehicles();
}
