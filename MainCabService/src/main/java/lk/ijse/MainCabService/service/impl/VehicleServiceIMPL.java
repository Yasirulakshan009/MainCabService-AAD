package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.VehicleDTO;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.enumeratios.Category;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.repository.VehicleCategoryRepository;
import lk.ijse.MainCabService.repository.VehicleRepository;
import lk.ijse.MainCabService.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceIMPL implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {

        log.info("Executing save Vehicle()");

        try{

            Vehicle vehicle = new Vehicle();

            vehicle.setVehicleModel(vehicleDTO.getVehicleName());
            vehicle.setPlateNO(vehicleDTO.getPlateNumber());
            vehicle.setLicenseNO(vehicleDTO.getLicenseNo());
            vehicle.setInsuranceNO(vehicleDTO.getInsuranceNo());
            vehicle.setDailyRate(vehicleDTO.getDailyPrice());
            vehicle.setSeats(vehicleDTO.getSeats());
            vehicle.setBags(vehicleDTO.getBags());

            vehicle.setVehicleTag(vehicleDTO.getTagClass());
            vehicle.setAcType(vehicleDTO.getAcType());
            vehicle.setVehicleStatus(vehicleDTO.getStatus());

            vehicle.setShowOnWebsite(vehicleDTO.isShowOnWebsite());
            vehicle.setWebCategory(vehicleDTO.getWebCategory());

            Category categoryEnum = vehicleDTO.getVehicleCategory();
            if (categoryEnum != null) {
                VehicleCategory vehicleCategory = vehicleCategoryRepository.findByVehicleCategory(categoryEnum);
                if (vehicleCategory != null) {
                    vehicle.setVehicleCategory(vehicleCategory);
                }
            }

            MultipartFile imageFile =(MultipartFile) vehicleDTO.getVehicleImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                vehicle.setVehicleImage(imageFile.getBytes());
            } else {
                vehicle.setVehicleImage(null);
            }

            vehicleRepository.save(vehicle);

            log.info("Vehicle saved successfully!");


        } catch (Exception e) {
            log.error("Error in save Vehicle(): " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateVehicle(VehicleDTO vehicleDTO) {

        log.info("Executing update Vehicle");

        try{

            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleDTO.getVehicleID());
            if(!optionalVehicle.isPresent()){
                throw new RuntimeException("vehicle not found!");
            }
            Vehicle vehicle = optionalVehicle.get();

            vehicle.setVehicleModel(vehicleDTO.getVehicleName());
            vehicle.setPlateNO(vehicleDTO.getPlateNumber());
            vehicle.setLicenseNO(vehicleDTO.getLicenseNo());
            vehicle.setInsuranceNO(vehicleDTO.getInsuranceNo());
            vehicle.setDailyRate(vehicleDTO.getDailyPrice());
            vehicle.setSeats(vehicleDTO.getSeats());
            vehicle.setBags(vehicleDTO.getBags());

            vehicle.setVehicleTag(vehicleDTO.getTagClass());
            vehicle.setAcType(vehicleDTO.getAcType());
            vehicle.setVehicleStatus(vehicleDTO.getStatus());

            vehicle.setShowOnWebsite(vehicleDTO.isShowOnWebsite());
            vehicle.setWebCategory(vehicleDTO.getWebCategory());

            Category categoryEnum = vehicleDTO.getVehicleCategory();
            if (categoryEnum != null) {
                VehicleCategory vehicleCategory = vehicleCategoryRepository.findByVehicleCategory(categoryEnum);
                if (vehicleCategory != null) {
                    vehicle.setVehicleCategory(vehicleCategory);
                }
            }

            MultipartFile imageFile = (MultipartFile) vehicleDTO.getVehicleImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                vehicle.setVehicleImage(imageFile.getBytes());
            }

            vehicleRepository.save(vehicle);

            log.info("Vehicle update successfully!");


        } catch (Exception e) {
            log.error("Error in update Vehicle(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteVehicle(Long id) {

        log.info("executing delete vehicle!");

        try {

            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);

            if(!optionalVehicle.isPresent()){
                throw new RuntimeException("vehicle not found with ID" + id);
            }

            vehicleRepository.deleteById(id);

            log.info("Vehicle deleted successfully for ID: " + id);

        } catch (Exception e) {
            log.error("error in deleted vehicle(): " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public VehicleDTO getVehicleById(Long id) {

        log.info("executing vehicle details!");

        try{

            Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);

            if(!optionalVehicle.isPresent()){
                throw new RuntimeException("vehicle not found with ID" + id);
            }

            Vehicle vehicle = optionalVehicle.get();

            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicleID(vehicle.getVehicleID());
            vehicleDTO.setVehicleName(vehicle.getVehicleModel());
            vehicleDTO.setPlateNumber(vehicle.getPlateNO());
            vehicleDTO.setLicenseNo(vehicle.getLicenseNO());
            vehicleDTO.setInsuranceNo(vehicle.getInsuranceNO());
            vehicleDTO.setDailyPrice(vehicle.getDailyRate());
            vehicleDTO.setSeats(vehicle.getSeats());
            vehicleDTO.setBags(vehicle.getBags());

            vehicleDTO.setTagClass(vehicle.getVehicleTag());
            vehicleDTO.setAcType(vehicle.getAcType());
            vehicleDTO.setStatus(vehicle.getVehicleStatus());

            vehicleDTO.setShowOnWebsite(vehicle.isShowOnWebsite());
            vehicleDTO.setWebCategory(vehicle.getWebCategory());

            VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
            if (vehicleCategory != null) {
                vehicleDTO.setVehicleCategory(vehicleCategory.getVehicleCategory());
            }

            byte[] imageBytes = vehicle.getVehicleImage();
            if (imageBytes != null && imageBytes.length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                vehicleDTO.setVehicleImage(base64Image);
            } else {
                vehicleDTO.setVehicleImage(null);
            }


            log.info("add" + vehicleDTO + " vehicle details successfully.");
            return vehicleDTO;

        } catch (Exception e) {
            log.error("error in vehicle details(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        log.info("Executing getAllVehicles()");

        try{

            List<Vehicle> vehicleList = vehicleRepository.findAll();
            List<VehicleDTO> vehicleDTOList = new ArrayList<>();

            for (Vehicle vehicle : vehicleList) {
                VehicleDTO vehicleDTO = new VehicleDTO();

                vehicleDTO.setVehicleID(vehicle.getVehicleID());
                vehicleDTO.setVehicleName(vehicle.getVehicleModel());
                vehicleDTO.setPlateNumber(vehicle.getPlateNO());
                vehicleDTO.setLicenseNo(vehicle.getLicenseNO());
                vehicleDTO.setInsuranceNo(vehicle.getInsuranceNO());
                vehicleDTO.setDailyPrice(vehicle.getDailyRate());
                vehicleDTO.setSeats(vehicle.getSeats());
                vehicleDTO.setBags(vehicle.getBags());

                vehicleDTO.setTagClass(vehicle.getVehicleTag());
                vehicleDTO.setAcType(vehicle.getAcType());
                vehicleDTO.setStatus(vehicle.getVehicleStatus());

                vehicleDTO.setShowOnWebsite(vehicle.isShowOnWebsite());
                vehicleDTO.setWebCategory(vehicle.getWebCategory());

                VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
                if (vehicleCategory != null) {
                    vehicleDTO.setVehicleCategory(vehicleCategory.getVehicleCategory());
                }

                byte[] imageBytes = vehicle.getVehicleImage();
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    vehicleDTO.setVehicleImage(base64Image);
                } else {
                    vehicleDTO.setVehicleImage(null);
                }

                vehicleDTOList.add(vehicleDTO);
            }

            log.info("add" + vehicleDTOList.size() + " vehicles successfully.");
            return vehicleDTOList;

        } catch (Exception e) {
            log.error("error in get AllVehicles(): " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<VehicleDTO> getVehiclesByStatus(VehicleStatus status) {

        log.info("Executing getVehicleByststus() with status" + status);

        try{

            List<Vehicle> vehicleList= vehicleRepository.findByVehicleStatus(status);
            List<VehicleDTO> vehicleDTOList = new ArrayList<>();

            for (Vehicle vehicle : vehicleList){
                VehicleDTO vehicleDTO = new VehicleDTO();

                vehicleDTO.setVehicleID(vehicle.getVehicleID());
                vehicleDTO.setVehicleName(vehicle.getVehicleModel());
                vehicleDTO.setPlateNumber(vehicle.getPlateNO());
                vehicleDTO.setLicenseNo(vehicle.getLicenseNO());
                vehicleDTO.setInsuranceNo(vehicle.getInsuranceNO());
                vehicleDTO.setDailyPrice(vehicle.getDailyRate());
                vehicleDTO.setSeats(vehicle.getSeats());
                vehicleDTO.setBags(vehicle.getBags());

                vehicleDTO.setTagClass(vehicle.getVehicleTag());
                vehicleDTO.setAcType(vehicle.getAcType());
                vehicleDTO.setStatus(vehicle.getVehicleStatus());

                vehicleDTO.setShowOnWebsite(vehicle.isShowOnWebsite());
                vehicleDTO.setWebCategory(vehicle.getWebCategory());

                VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
                if (vehicleCategory != null) {
                    vehicleDTO.setVehicleCategory(vehicleCategory.getVehicleCategory());
                }

                byte[] imageBytes = vehicle.getVehicleImage();
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    vehicleDTO.setVehicleImage(base64Image);
                } else {
                    vehicleDTO.setVehicleImage(null);
                }

                vehicleDTOList.add(vehicleDTO);
            }

            log.info("Add " + vehicleDTOList.size() + " vehicles successfully for status: " + status);
            return vehicleDTOList;

        } catch (Exception e) {
            log.error("error in getVehicleByStatus" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VehicleDTO> searchVehicles(String keyword) {
        log.info("Executing searchVehicles() with keyword: " + keyword);

        try {
            List<Vehicle> vehicleList = vehicleRepository.searchVehicles(keyword);
            List<VehicleDTO> vehicleDTOList = new ArrayList<>();

            for (Vehicle vehicle : vehicleList) {
                VehicleDTO vehicleDTO = new VehicleDTO();

                vehicleDTO.setVehicleID(vehicle.getVehicleID());
                vehicleDTO.setVehicleName(vehicle.getVehicleModel());
                vehicleDTO.setPlateNumber(vehicle.getPlateNO());
                vehicleDTO.setLicenseNo(vehicle.getLicenseNO());
                vehicleDTO.setInsuranceNo(vehicle.getInsuranceNO());
                vehicleDTO.setDailyPrice(vehicle.getDailyRate());
                vehicleDTO.setSeats(vehicle.getSeats());
                vehicleDTO.setBags(vehicle.getBags());

                vehicleDTO.setTagClass(vehicle.getVehicleTag());
                vehicleDTO.setAcType(vehicle.getAcType());
                vehicleDTO.setStatus(vehicle.getVehicleStatus());

                vehicleDTO.setShowOnWebsite(vehicle.isShowOnWebsite());
                vehicleDTO.setWebCategory(vehicle.getWebCategory());

                VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
                if (vehicleCategory != null) {
                    vehicleDTO.setVehicleCategory(vehicleCategory.getVehicleCategory());
                }

                byte[] imageBytes = vehicle.getVehicleImage();
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    vehicleDTO.setVehicleImage(base64Image);
                } else {
                    vehicleDTO.setVehicleImage(null);
                }

                vehicleDTOList.add(vehicleDTO);
            }

            log.info("Found " + vehicleDTOList.size() + " vehicles for keyword: " + keyword);
            return vehicleDTOList;

        } catch (Exception e) {
            log.error("Error in searchVehicles(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getVehicleCountByStatus(VehicleStatus status) {
        log.info("Executing getVehicleCountByStatus() for status: " + status);
        try {
            return vehicleRepository.countByVehicleStatus(status);
        } catch (Exception e) {
            log.error("Error in getVehicleCountByStatus(): " + e.getMessage());
            return 0;
        }
    }

    @Override
    public long getTotalVehicleCount() {
        log.info("Executing getTotalVehicleCount()");
        try {
            return vehicleRepository.count();
        } catch (Exception e) {
            log.error("Error in getTotalVehicleCount(): " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<VehicleDTO> getWebsiteVehicles() {
        log.info("Executing getWebsiteVehicles()");
        try {
            List<Vehicle> vehicleList = vehicleRepository.findByShowOnWebsiteTrue();
            List<VehicleDTO> vehicleDTOList = new ArrayList<>();

            for (Vehicle vehicle : vehicleList) {
                VehicleDTO vehicleDTO = new VehicleDTO();

                vehicleDTO.setVehicleID(vehicle.getVehicleID());
                vehicleDTO.setVehicleName(vehicle.getVehicleModel());
                vehicleDTO.setPlateNumber(vehicle.getPlateNO());
                vehicleDTO.setDailyPrice(vehicle.getDailyRate());
                vehicleDTO.setSeats(vehicle.getSeats());
                vehicleDTO.setBags(vehicle.getBags());
                vehicleDTO.setTagClass(vehicle.getVehicleTag());
                vehicleDTO.setAcType(vehicle.getAcType());
                vehicleDTO.setStatus(vehicle.getVehicleStatus());
                vehicleDTO.setShowOnWebsite(vehicle.isShowOnWebsite());
                vehicleDTO.setWebCategory(vehicle.getWebCategory());

                VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
                if (vehicleCategory != null) {
                    vehicleDTO.setVehicleCategory(vehicleCategory.getVehicleCategory());
                }

                byte[] imageBytes = vehicle.getVehicleImage();
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    vehicleDTO.setVehicleImage(base64Image);
                } else {
                    vehicleDTO.setVehicleImage(null);
                }

                vehicleDTOList.add(vehicleDTO);
            }

            return vehicleDTOList;
        } catch (Exception e) {
            log.error("Error in getWebsiteVehicles(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
