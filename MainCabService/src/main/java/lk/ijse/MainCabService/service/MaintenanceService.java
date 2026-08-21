package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.MaintenanceDTO;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;

import java.util.List;

public interface MaintenanceService {

    void saveMaintenance(MaintenanceDTO maintenanceDTO);

    void updateMaintenance(MaintenanceDTO maintenanceDTO);

    void deleteMaintenance(Long id);

    MaintenanceDTO getMaintenanceById(Long id);

    List<MaintenanceDTO> getAllMaintenance();

    List<MaintenanceDTO> getMaintenanceByStatus(MaintenanceStatus maintenanceStatus);
}
