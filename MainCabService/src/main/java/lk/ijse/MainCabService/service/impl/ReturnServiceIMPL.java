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

        log.info("Executing update return()");

        try {

            Return returnVehicle = returnRepository.findById(returnDTO.getReturnID())
                    .orElseThrow(() -> new RuntimeException("Return not found with ID: " + returnDTO.getReturnID()));

            Rental rental = returnVehicle.getRental();

            returnVehicle.setNotes(returnDTO.getNotes());
            returnVehicle.setExtraCharges(returnDTO.getExtraCharges());

            double finalAmount = rental.getTotalAmount() + returnDTO.getExtraCharges();
            returnVehicle.setFinalAmount(finalAmount);

            ReturnStatus returnStatus = returnDTO.getReturnStatus();
            returnVehicle.setReturnStatus(returnStatus);

            Method paymentMethodEnum = returnDTO.getPaymentMethod();
            if (paymentMethodEnum != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(paymentMethodEnum);
                if (paymentMethod != null) {
                    returnVehicle.setPaymentMethod(paymentMethod);
                }
            }

            returnRepository.save(returnVehicle);

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setRentalID(rental.getRentalID());
            paymentDTO.setAmount(finalAmount);
            paymentDTO.setPaymentMethod(paymentMethodEnum);
            paymentDTO.setStatus(PaymentStatus.COMPLETED);

            paymentService.updatePayment(paymentDTO);

            log.info("Return and payment updated successfully!");

        } catch (Exception e) {
            log.error("Error in update Return(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReturn(Long id) {

        log.info("Deleting return with ID: " + id);
        try {
            Return returnVehicle = returnRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Return not found with ID: " + id));

            Rental rental = returnVehicle.getRental();
            if (rental != null && rental.getVehicles() != null) {
                Vehicle vehicle = rental.getVehicles();
                vehicle.setVehicleStatus(VehicleStatus.RENTED);
                vehicleRepository.save(vehicle);
            }

            returnRepository.deleteById(id);
            log.info("Return deleted successfully!");

        } catch (Exception e) {
            log.error("Error in delete Return(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReturnDTO getReturnById(Long id) {

        log.info("Fetching return by ID: " + id);

        try {
            Return returnVehicle = returnRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Return not found with ID: " + id));

            ReturnDTO dto = new ReturnDTO();
            dto.setReturnID(returnVehicle.getReturnID());
            dto.setReturnDate(returnVehicle.getReturnDate());
            dto.setInitialReturnDate(returnVehicle.getInitialReturnDate());
            dto.setNotes(returnVehicle.getNotes());
            dto.setExtraCharges(returnVehicle.getExtraCharges());
            dto.setFinalAmount(returnVehicle.getFinalAmount());
            dto.setReturnStatus(returnVehicle.getReturnStatus());

            if (returnVehicle.getRental() != null) {
                dto.setRentalID(returnVehicle.getRental().getRentalID());
            }
            if (returnVehicle.getPaymentMethod() != null) {
                dto.setPaymentMethod(returnVehicle.getPaymentMethod().getPaymentMethod());
            }

            return dto;

        } catch (Exception e) {
            log.error("Error in getReturnById(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReturnDTO> getAllReturns() {

        log.info("Executing all returns");

        try {
            List<Return> returns = returnRepository.findAll();
            List<ReturnDTO> returnDTOs = new java.util.ArrayList<>();

            for (Return returnVehicle : returns) {
                ReturnDTO dto = new ReturnDTO();
                dto.setReturnID(returnVehicle.getReturnID());
                dto.setReturnDate(returnVehicle.getReturnDate());
                dto.setInitialReturnDate(returnVehicle.getInitialReturnDate());
                dto.setNotes(returnVehicle.getNotes());
                dto.setExtraCharges(returnVehicle.getExtraCharges());
                dto.setFinalAmount(returnVehicle.getFinalAmount());
                dto.setReturnStatus(returnVehicle.getReturnStatus());

                if (returnVehicle.getRental() != null) {
                    dto.setRentalID(returnVehicle.getRental().getRentalID());
                }
                if (returnVehicle.getPaymentMethod() != null) {
                    dto.setPaymentMethod(returnVehicle.getPaymentMethod().getPaymentMethod());
                }

                returnDTOs.add(dto);
            }
            return returnDTOs;

        } catch (Exception e) {
            log.error("Error in getAllReturns(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReturnDTO> searchReturns(String keyword) {

        log.info("Searching returns with rental ID keyword: " + keyword);

        try {
            List<ReturnDTO> returnDTOs = new java.util.ArrayList<>();
            Long rentalId = Long.parseLong(keyword);

            List<Return> returns = returnRepository.findByRental_RentalID(rentalId);

            for (Return returnVehicle : returns) {
                ReturnDTO dto = new ReturnDTO();
                dto.setReturnID(returnVehicle.getReturnID());
                dto.setReturnDate(returnVehicle.getReturnDate());
                dto.setInitialReturnDate(returnVehicle.getInitialReturnDate());
                dto.setNotes(returnVehicle.getNotes());
                dto.setExtraCharges(returnVehicle.getExtraCharges());
                dto.setFinalAmount(returnVehicle.getFinalAmount());
                dto.setReturnStatus(returnVehicle.getReturnStatus());

                if (returnVehicle.getRental() != null) {
                    dto.setRentalID(returnVehicle.getRental().getRentalID());
                }
                if (returnVehicle.getPaymentMethod() != null) {
                    dto.setPaymentMethod(returnVehicle.getPaymentMethod().getPaymentMethod());
                }

                returnDTOs.add(dto);
            }
            return returnDTOs;

        } catch (NumberFormatException e) {
            log.warn("Keyword is not a valid number for rental ID search: " + keyword);
            return new java.util.ArrayList<>();
        } catch (Exception e) {
            log.error("Error in searchReturns(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReturnDTO> getReturnsByStatus(ReturnStatus returnStatus) {

        log.info("Executing returns by status: " + returnStatus);

        try {
            List<Return> returns = returnRepository.findByReturnStatus(returnStatus);
            List<ReturnDTO> returnDTOs = new java.util.ArrayList<>();

            for (Return returnVehicle : returns) {
                ReturnDTO dto = new ReturnDTO();
                dto.setReturnID(returnVehicle.getReturnID());
                dto.setReturnDate(returnVehicle.getReturnDate());
                dto.setInitialReturnDate(returnVehicle.getInitialReturnDate());
                dto.setNotes(returnVehicle.getNotes());
                dto.setExtraCharges(returnVehicle.getExtraCharges());
                dto.setFinalAmount(returnVehicle.getFinalAmount());
                dto.setReturnStatus(returnVehicle.getReturnStatus());

                if (returnVehicle.getRental() != null) {
                    dto.setRentalID(returnVehicle.getRental().getRentalID());
                }
                if (returnVehicle.getPaymentMethod() != null) {
                    dto.setPaymentMethod(returnVehicle.getPaymentMethod().getPaymentMethod());
                }

                returnDTOs.add(dto);
            }
            return returnDTOs;

        } catch (Exception e) {
            log.error("Error in getReturnsByStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getReturnCountByStatus(ReturnStatus returnStatus) {

        try {
            return returnRepository.countByReturnStatus(returnStatus);
        } catch (Exception e) {
            log.error("Error in getReturnCountByStatus(): " + e.getMessage());
            return 0;
        }
    }

    @Override
    public long getTotalReturnCount() {

        try {
            return returnRepository.count();
        } catch (Exception e) {
            log.error("Error in getTotalReturnCount(): " + e.getMessage());
            return 0;
        }
    }
}
