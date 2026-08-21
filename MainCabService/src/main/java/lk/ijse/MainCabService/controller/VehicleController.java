package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.VehicleDTO;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveVehicle(@ModelAttribute VehicleDTO vehicleDTO) {
        vehicleService.saveVehicle(vehicleDTO);
        return "Vehicle saved successfully";
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateVehicle(@ModelAttribute VehicleDTO vehicleDTO) {
        vehicleService.updateVehicle(vehicleDTO);
        return "Vehicle update successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return "Vehicle deleted successfully";
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDTO getVehicleById(@PathVariable Long id){
        return vehicleService.getVehicleById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllVehicles(){
       return vehicleService.getAllVehicles();
    }

    @GetMapping(value = "/status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getVehiclesByStatus(@PathVariable VehicleStatus status){
        return vehicleService.getVehiclesByStatus(status);
    }

    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> searchVehicle(@RequestParam("keyword") String keyword){
        return vehicleService.searchVehicles(keyword);
    }

    @GetMapping(value = "/count/status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    public long getVehicleCountByStatus(@PathVariable VehicleStatus status){
        return vehicleService.getVehicleCountByStatus(status);
    }

    @GetMapping(value = "/count",produces = MediaType.APPLICATION_JSON_VALUE)
    public long getTotalVehicleCount(){
        return vehicleService.getTotalVehicleCount();
    }


}
