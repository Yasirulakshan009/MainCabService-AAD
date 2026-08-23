package lk.ijse.MainCabService.service.impl;


import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.dto.ReturnDTO;
import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.entity.Return;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.enumeratios.*;
import lk.ijse.MainCabService.repository.*;
import lk.ijse.MainCabService.service.PaymentService;
import lk.ijse.MainCabService.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReturnServiceIMPL implements ReturnService {

    private final ReturnRepository returnRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentService paymentService;


    @Override
    public void saveReturn(ReturnDTO returnDTO) {

        log.info("Executing save return()");

        try{

            Return returnVehicle = new Return();

            Rental rental = rentalRepository.findById(returnDTO.getRentalID())
                    .orElseThrow(() -> new RuntimeException("rental not found with ID" + returnDTO.getRentalID()));
            returnVehicle.setRental(rental);

            returnVehicle.setReturnDate(LocalDate.now());
            returnVehicle.setInitialReturnDate(rental.getEndDate());
            returnVehicle.setNotes(returnDTO.getNotes());
            returnVehicle.setExtraCharges(returnDTO.getExtraCharges());

            double finalAmount = rental.getTotalAmount() + returnDTO.getExtraCharges();
            returnVehicle.setFinalAmount(finalAmount);

            ReturnStatus returnStatus = returnDTO.getReturnStatus();
            returnVehicle.setReturnStatus(returnStatus);

            Method paymentMethodEnum = returnDTO.getPaymentMethod();
            if(paymentMethodEnum != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(paymentMethodEnum);
                if(paymentMethod != null){
                    returnVehicle.setPaymentMethod(paymentMethod);
                }
            }

            Vehicle vehicle = rental.getVehicles();
            if(vehicle != null){
                vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
                vehicleRepository.save(vehicle);
            }

            rental.setRentalStatus(RentalStatus.COMPLETED);
            rentalRepository.save(rental);

            returnRepository.save(returnVehicle);

            /*?////////////////////////////?*/

            PaymentDTO paymentDTO = new PaymentDTO();

            paymentDTO.setRentalID(rental.getRentalID());
            paymentDTO.setAmount(finalAmount);
            paymentDTO.setPaymentMethod(paymentMethodEnum);
            paymentDTO.setStatus(PaymentStatus.COMPLETED);

            paymentService.updatePayment(paymentDTO);

            log.info("Return saved, vehicle status updated, rental completed, and payment updated successfully!");


        } catch (Exception e) {
            log.error("Error in save Return(): " + e.getMessage());
            throw new RuntimeException(e);
        }

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
