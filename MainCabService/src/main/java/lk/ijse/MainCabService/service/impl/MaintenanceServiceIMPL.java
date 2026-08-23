package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.MaintenanceDTO;
import lk.ijse.MainCabService.entity.Maintenance;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.enumeratios.MaintenanceStatus;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.repository.MaintenanceRepository;
import lk.ijse.MainCabService.repository.VehicleRepository;
import lk.ijse.MainCabService.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceIMPL implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public void saveMaintenance(MaintenanceDTO maintenanceDTO) {

        log.info("Executing save Maintenance()");

        try{

            Maintenance maintenance = new Maintenance();

            maintenance.setTitle(maintenanceDTO.getTitle());
            maintenance.setDescription(maintenanceDTO.getDescription());
            maintenance.setPriority(maintenanceDTO.getPriority());
            maintenance.setMaintenanceStatus(maintenanceDTO.getMaintenanceStatus());
            maintenance.setScheduledDate(maintenanceDTO.getScheduledDate());
            maintenance.setCost(maintenanceDTO.getCost());
            maintenance.setVendor(maintenanceDTO.getVendor());

            MaintenanceStatus status = maintenanceDTO.getMaintenanceStatus();
            maintenance.setMaintenanceStatus(status);

            Vehicle vehicle = vehicleRepository.findById(maintenanceDTO.getVehicleID())
            .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + maintenanceDTO.getVehicleID()));

            if (status == MaintenanceStatus.IN_PROGRESS) {
                vehicle.setVehicleStatus(VehicleStatus.MAINTENANCE);
                vehicleRepository.save(vehicle);
            } else if (status == MaintenanceStatus.COMPLETED) {
                vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
                vehicleRepository.save(vehicle);
            }

            maintenance.setVehicle(vehicle);

            maintenanceRepository.save(maintenance);
            log.info("Maintenance saved successfully!");

        } catch (Exception e) {
            log.error("Error in save Maintenance(): " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateMaintenance(MaintenanceDTO maintenanceDTO) {

        log.info("Executing update Maintenance()");

        try {

            Maintenance maintenance = maintenanceRepository.findById(maintenanceDTO.getMaintenanceID())
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with ID: " + maintenanceDTO.getMaintenanceID()));

            maintenance.setTitle(maintenanceDTO.getTitle());
            maintenance.setDescription(maintenanceDTO.getDescription());
            maintenance.setPriority(maintenanceDTO.getPriority());
            maintenance.setScheduledDate(maintenanceDTO.getScheduledDate());
            maintenance.setCost(maintenanceDTO.getCost());
            maintenance.setVendor(maintenanceDTO.getVendor());

            MaintenanceStatus newStatus = maintenanceDTO.getMaintenanceStatus();
            maintenance.setMaintenanceStatus(newStatus);

            Vehicle vehicle = vehicleRepository.findById(maintenanceDTO.getVehicleID())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + maintenanceDTO.getVehicleID()));

            maintenance.setVehicle(vehicle);

            if (newStatus == MaintenanceStatus.IN_PROGRESS) {
                vehicle.setVehicleStatus(VehicleStatus.MAINTENANCE);
                vehicleRepository.save(vehicle);
            } else if (newStatus == MaintenanceStatus.COMPLETED) {
                vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
                vehicleRepository.save(vehicle);
            }

            maintenanceRepository.save(maintenance);
            log.info("Maintenance updated successfully!");

        } catch (Exception e) {
            log.error("Error in update Maintenance(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMaintenance(Long id) {

        log.info("Executing delete Maintenance() for ID: " + id);
        try {
            Maintenance maintenance = maintenanceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with ID: " + id));

            if (maintenance.getVehicle() != null) {
                Vehicle vehicle = maintenance.getVehicle();
                if (vehicle.getVehicleStatus() == VehicleStatus.MAINTENANCE) {
                    vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
                    vehicleRepository.save(vehicle);
                }
            }

            maintenanceRepository.deleteById(id);
            log.info("Maintenance deleted successfully!");

        } catch (Exception e) {
            log.error("Error in delete Maintenance(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public MaintenanceDTO getMaintenanceById(Long id) {
        log.info("Fetching maintenance by ID: " + id);
        try {
            Maintenance maintenance = maintenanceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Maintenance not found with ID: " + id));

            MaintenanceDTO dto = new MaintenanceDTO();
            dto.setMaintenanceID(maintenance.getMaintenanceID());
            dto.setTitle(maintenance.getTitle());
            dto.setDescription(maintenance.getDescription());
            dto.setPriority(maintenance.getPriority());
            dto.setMaintenanceStatus(maintenance.getMaintenanceStatus());
            dto.setScheduledDate(maintenance.getScheduledDate());
            dto.setCost(maintenance.getCost());
            dto.setVendor(maintenance.getVendor());

            if (maintenance.getVehicle() != null) {
                dto.setVehicleID(maintenance.getVehicle().getVehicleID());
            }

            return dto;

        } catch (Exception e) {
            log.error("Error in getMaintenanceById(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MaintenanceDTO> getAllMaintenance() {
        log.info("Fetching all maintenance records");
        try {
            List<Maintenance> maintenanceList = maintenanceRepository.findAll();
            List<MaintenanceDTO> dtoList = new java.util.ArrayList<>();

            for (Maintenance maintenance : maintenanceList) {
                MaintenanceDTO dto = new MaintenanceDTO();
                dto.setMaintenanceID(maintenance.getMaintenanceID());
                dto.setTitle(maintenance.getTitle());
                dto.setDescription(maintenance.getDescription());
                dto.setPriority(maintenance.getPriority());
                dto.setMaintenanceStatus(maintenance.getMaintenanceStatus());
                dto.setScheduledDate(maintenance.getScheduledDate());
                dto.setCost(maintenance.getCost());
                dto.setVendor(maintenance.getVendor());

                if (maintenance.getVehicle() != null) {
                    dto.setVehicleID(maintenance.getVehicle().getVehicleID());
                }

                dtoList.add(dto);
            }
            return dtoList;

        } catch (Exception e) {
            log.error("Error in getAllMaintenance(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MaintenanceDTO> getMaintenanceByStatus(MaintenanceStatus maintenanceStatus) {
        log.info("Fetching maintenance records by status: " + maintenanceStatus);

        try {
            List<Maintenance> maintenanceList = maintenanceRepository.findByMaintenanceStatus(maintenanceStatus);
            List<MaintenanceDTO> dtoList = new java.util.ArrayList<>();

            for (Maintenance maintenance : maintenanceList) {
                MaintenanceDTO dto = new MaintenanceDTO();
                dto.setMaintenanceID(maintenance.getMaintenanceID());
                dto.setTitle(maintenance.getTitle());
                dto.setDescription(maintenance.getDescription());
                dto.setPriority(maintenance.getPriority());
                dto.setMaintenanceStatus(maintenance.getMaintenanceStatus());
                dto.setScheduledDate(maintenance.getScheduledDate());
                dto.setCost(maintenance.getCost());
                dto.setVendor(maintenance.getVendor());

                if (maintenance.getVehicle() != null) {
                    dto.setVehicleID(maintenance.getVehicle().getVehicleID());
                }

                dtoList.add(dto);
            }
            return dtoList;

        } catch (Exception e) {
            log.error("Error in getMaintenanceByStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
