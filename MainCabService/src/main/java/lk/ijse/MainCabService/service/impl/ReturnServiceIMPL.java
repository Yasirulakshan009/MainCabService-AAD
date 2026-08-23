package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.ReturnDTO;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;
import lk.ijse.MainCabService.repository.PaymentRepository;
import lk.ijse.MainCabService.repository.RentalRepository;
import lk.ijse.MainCabService.repository.ReturnRepository;
import lk.ijse.MainCabService.repository.VehicleRepository;
import lk.ijse.MainCabService.service.PaymentService;
import lk.ijse.MainCabService.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnServiceIMPL implements ReturnService {

    private final ReturnRepository returnRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;


    @Override
    public void saveReturn(ReturnDTO returnDTO) {


    }

    @Override
    public void updateReturn(ReturnDTO returnDTO) {

    }

    @Override
    public void deleteReturn(Long id) {

    }

    @Override
    public ReturnDTO getReturnById(Long id) {
        return null;
    }

    @Override
    public List<ReturnDTO> getAllReturns() {
        return List.of();
    }

    @Override
    public List<ReturnDTO> searchReturns(String keyword) {
        return List.of();
    }

    @Override
    public List<ReturnDTO> getReturnsByStatus(ReturnStatus returnStatus) {
        return List.of();
    }

    @Override
    public long getReturnCountByStatus(ReturnStatus returnStatus) {
        return 0;
    }

    @Override
    public long getTotalReturnCount() {
        return 0;
    }
}
