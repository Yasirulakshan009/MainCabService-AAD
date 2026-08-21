package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.RentalDTO;
import lk.ijse.MainCabService.enumeratios.RentalStatus;
import lk.ijse.MainCabService.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/rentals")
public class RentalController {

    private final RentalService rentalService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveRental(@RequestBody RentalDTO rentalDTO) {
        rentalService.saveRental(rentalDTO);
        return "Rental saved successfully";
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateRental(@RequestBody RentalDTO rentalDTO) {
        rentalService.updateRental(rentalDTO);
        return "Rental updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return "Rental deleted successfully";
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalDTO getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentalDTO> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentalDTO> getRentalsByStatus(@PathVariable RentalStatus status) {
        return rentalService.getRentalsByStatus(status);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentalDTO> searchRentals(@RequestParam("keyword") String keyword) {
        return rentalService.searchRentals(keyword);
    }

    @GetMapping(value = "/count/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getRentalCountByStatus(@PathVariable RentalStatus status) {
        return rentalService.getRentalCountByStatus(status);
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getTotalRentalCount() {
        return rentalService.getTotalRentalCount();
    }
}