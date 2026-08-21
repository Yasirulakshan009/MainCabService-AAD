package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.MaintenanceDTO;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import lk.ijse.MainCabService.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        maintenanceService.saveMaintenance(maintenanceDTO);
        return "Maintenance saved successfully";
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        maintenanceService.updateMaintenance(maintenanceDTO);
        return "Maintenance updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return "Maintenance deleted successfully";
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MaintenanceDTO getMaintenanceById(@PathVariable Long id) {
        return maintenanceService.getMaintenanceById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaintenanceDTO> getAllMaintenance() {
        return maintenanceService.getAllMaintenance();
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaintenanceDTO> getMaintenanceByStatus(@PathVariable MaintenanceStatus status) {
        return maintenanceService.getMaintenanceByStatus(status);
    }
}