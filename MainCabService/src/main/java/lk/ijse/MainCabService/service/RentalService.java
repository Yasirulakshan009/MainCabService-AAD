package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.RentalDTO;
import lk.ijse.MainCabService.enumeratios.RentalStatus;

import java.util.List;

public interface RentalService {

    void saveRental(RentalDTO rentalDTO);
    void updateRental(RentalDTO rentalDTO);
    RentalDTO getRentalById(Long id);
    List<RentalDTO> getAllRentals();
    List<RentalDTO> searchRentals(String keyword);
    List<RentalDTO> getRentalsByStatus(RentalStatus status);
    long getRentalCountByStatus(RentalStatus rentalStatus);
    void deleteRental(Long id);
    long getTotalRentalCount();

}
