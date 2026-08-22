package lk.ijse.MainCabService.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.MainCabService.dto.PaymentDTO;
import lk.ijse.MainCabService.dto.RentalDTO;
import lk.ijse.MainCabService.entity.Customer;
import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.PaymentStatus;
import lk.ijse.MainCabService.enumeratios.RentalStatus;
import lk.ijse.MainCabService.enumeratios.VehicleStatus;
import lk.ijse.MainCabService.repository.CustomerRepository;
import lk.ijse.MainCabService.repository.PaymentMethodRepository;
import lk.ijse.MainCabService.repository.RentalRepository;
import lk.ijse.MainCabService.repository.VehicleRepository;
import lk.ijse.MainCabService.service.PaymentService;
import lk.ijse.MainCabService.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RentalServiceIMPL implements RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentService paymentService;


    @Override
    public void saveRental(RentalDTO rentalDTO) {

        log.info("Executing save Rental()");

        try{

            Rental rental = new Rental();
            rental.setStartDate(rentalDTO.getStartDate());
            rental.setEndDate(rentalDTO.getEndDate());
            rental.setPickupAddress(rentalDTO.getPickupAddress());
            rental.setDeliveryFee(rentalDTO.getDeliveryFee());
            rental.setRentalStatus(RentalStatus.ACTIVE);

            Vehicle vehicle = vehicleRepository.findById(rentalDTO.getVehicleID())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + rentalDTO.getVehicleID()));
            vehicle.setVehicleStatus(VehicleStatus.RENTED);
            vehicleRepository.save(vehicle);
            rental.setVehicles(vehicle);

            long days = java.time.temporal.ChronoUnit.DAYS.between(rentalDTO.getStartDate(), rentalDTO.getEndDate());
            if (days <= 0) {
                days = 1;
            }

            double dailyRate = vehicle.getDailyRate();

            double totalAmount = (days * dailyRate) + rentalDTO.getDeliveryFee();
            rental.setTotalAmount(totalAmount);

            Customer customer = customerRepository.findById(rentalDTO.getCustomerID())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + rentalDTO.getCustomerID()));
            rental.setCustomer(customer);


            Method paymentMethodEnum = rentalDTO.getPaymentMethod();
            if (paymentMethodEnum != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(paymentMethodEnum);
                if (paymentMethod != null) {
                    rental.setPaymentMethod(paymentMethod);
                }
            }

            Rental savedRental = rentalRepository.save(rental);

            /*/////////////////////////*/

            PaymentDTO paymentDTO = new PaymentDTO();

            paymentDTO.setRentalID(savedRental.getRentalID());
            paymentDTO.setAmount(savedRental.getTotalAmount());
            paymentDTO.setPaymentMethod(paymentMethodEnum);

            paymentService.savePayment(paymentDTO);


            log.info("Rental and payment saved successfully!");

        } catch (Exception e) {
            log.error("Error in save Rental(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRental(RentalDTO rentalDTO) {

        log.info("Executing update Rental()");

        Rental rental = rentalRepository.findById(rentalDTO.getRentalID())
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + rentalDTO.getRentalID()));

        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setPickupAddress(rentalDTO.getPickupAddress());
        rental.setDeliveryFee(rentalDTO.getDeliveryFee());
        rental.setTotalAmount(rentalDTO.getTotalAmount());
        rental.setRentalStatus(rentalDTO.getRentalStatus());

        Customer customer = customerRepository.findById(rentalDTO.getCustomerID())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        rental.setCustomer(customer);

        Vehicle vehicle = vehicleRepository.findById(rentalDTO.getVehicleID())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        rental.setVehicles(vehicle);

        RentalStatus newStatus = rentalDTO.getRentalStatus();

        if (newStatus == RentalStatus.COMPLETED || newStatus == RentalStatus.CANCELLED) {
            vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        } else if (newStatus == RentalStatus.ACTIVE) {
            vehicle.setVehicleStatus(VehicleStatus.RENTED);
            vehicleRepository.save(vehicle);
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(rentalDTO.getStartDate(), rentalDTO.getEndDate());
        if (days <= 0) {
            days = 1;
        }

        double dailyRate = vehicle.getDailyRate();
        double updatedTotalAmount = (days * dailyRate) + rentalDTO.getDeliveryFee();

        rental.setTotalAmount(updatedTotalAmount);

        Method paymentMethodEnum = rentalDTO.getPaymentMethod();
        if (paymentMethodEnum != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethod(paymentMethodEnum);
            if (paymentMethod != null) {
                rental.setPaymentMethod(paymentMethod);
            }
        }

        Rental updatedRental = rentalRepository.save(rental);

        /*////////////////*/

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRentalID(updatedRental.getRentalID());
        paymentDTO.setAmount(updatedRental.getTotalAmount());
        paymentDTO.setPaymentMethod(paymentMethodEnum);

        paymentService.updatePayment(paymentDTO);
        log.info("Rental and payment updated successfully!");
    }

    @Override
    public RentalDTO getRentalById(Long id) {
        log.info("Fetching rental by ID: " + id);

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + id));

        RentalDTO dto = new RentalDTO();
        dto.setRentalID(rental.getRentalID());
        dto.setStartDate(rental.getStartDate());
        dto.setEndDate(rental.getEndDate());
        dto.setPickupAddress(rental.getPickupAddress());
        dto.setDeliveryFee(rental.getDeliveryFee());
        dto.setTotalAmount(rental.getTotalAmount());
        dto.setRentalStatus(rental.getRentalStatus());

        if (rental.getCustomer() != null) {
            dto.setCustomerID(rental.getCustomer().getCustomerID());
        }
        if (rental.getVehicles() != null) {
            dto.setVehicleID(rental.getVehicles().getVehicleID());
        }
        if (rental.getPaymentMethod() != null) {
            dto.setPaymentMethod(rental.getPaymentMethod().getPaymentMethod());
        }

        return dto;
    }

    @Override
    public List<RentalDTO> getAllRentals() {

        log.info("Fetching all rentals");

        try{

            List<Rental> rentals = rentalRepository.findAll();
            List<RentalDTO> rentalDTOS = new java.util.ArrayList<>();

            for (Rental rental : rentals) {
                RentalDTO dto = new RentalDTO();
                dto.setRentalID(rental.getRentalID());
                dto.setStartDate(rental.getStartDate());
                dto.setEndDate(rental.getEndDate());
                dto.setPickupAddress(rental.getPickupAddress());
                dto.setDeliveryFee(rental.getDeliveryFee());
                dto.setTotalAmount(rental.getTotalAmount());
                dto.setRentalStatus(rental.getRentalStatus());

                if (rental.getCustomer() != null) {
                    dto.setCustomerID(rental.getCustomer().getCustomerID());
                }
                if (rental.getVehicles() != null) {
                    dto.setVehicleID(rental.getVehicles().getVehicleID());
                }
                if (rental.getPaymentMethod() != null) {
                    dto.setPaymentMethod(rental.getPaymentMethod().getPaymentMethod());
                }

                rentalDTOS.add(dto);
            }
            return rentalDTOS;

        } catch (Exception e) {
            log.error("Error in all Rentals(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RentalDTO> searchRentals(String keyword) {

        log.info("Searching rentals with keyword (ID): " + keyword);

        try {

            List<Rental> rentals = new java.util.ArrayList<>();

            try {

                Long id = Long.parseLong(keyword);
                rentals = rentalRepository.findByRentalIDOrVehicleID(id);

            } catch (NumberFormatException e) {
                log.warn("Keyword is not a valid number for ID search: " + keyword);
            }

            List<RentalDTO> rentalDTOs = new java.util.ArrayList<>();

            for (Rental rental : rentals) {
                RentalDTO dto = new RentalDTO();
                dto.setRentalID(rental.getRentalID());
                dto.setStartDate(rental.getStartDate());
                dto.setEndDate(rental.getEndDate());
                dto.setPickupAddress(rental.getPickupAddress());
                dto.setDeliveryFee(rental.getDeliveryFee());
                dto.setTotalAmount(rental.getTotalAmount());
                dto.setRentalStatus(rental.getRentalStatus());

                if (rental.getCustomer() != null) {
                    dto.setCustomerID(rental.getCustomer().getCustomerID());
                }
                if (rental.getVehicles() != null) {
                    dto.setVehicleID(rental.getVehicles().getVehicleID());
                }
                if (rental.getPaymentMethod() != null) {
                    dto.setPaymentMethod(rental.getPaymentMethod().getPaymentMethod());
                }

                rentalDTOs.add(dto);
            }
            return rentalDTOs;

        } catch (Exception e) {
            log.error("Error in searchRentals(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RentalDTO> getRentalsByStatus(RentalStatus status) {

        log.info("Fetching rentals by status: " + status);

        try {
            List<Rental> rentals = rentalRepository.findByRentalStatus(status);
            List<RentalDTO> rentalDTOs = new java.util.ArrayList<>();

            for (Rental rental : rentals) {
                RentalDTO dto = new RentalDTO();
                dto.setRentalID(rental.getRentalID());
                dto.setStartDate(rental.getStartDate());
                dto.setEndDate(rental.getEndDate());
                dto.setPickupAddress(rental.getPickupAddress());
                dto.setDeliveryFee(rental.getDeliveryFee());
                dto.setTotalAmount(rental.getTotalAmount());
                dto.setRentalStatus(rental.getRentalStatus());

                if (rental.getCustomer() != null) {
                    dto.setCustomerID(rental.getCustomer().getCustomerID());
                }
                if (rental.getVehicles() != null) {
                    dto.setVehicleID(rental.getVehicles().getVehicleID());
                }
                if (rental.getPaymentMethod() != null) {
                    dto.setPaymentMethod(rental.getPaymentMethod().getPaymentMethod());
                }

                rentalDTOs.add(dto);
            }
            return rentalDTOs;

        } catch (Exception e) {
            log.error("Error in getRentalsByStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getRentalCountByStatus(RentalStatus rentalStatus) {

        log.info("Getting rental count by status: " + rentalStatus);

        try {
            return rentalRepository.countByRentalStatus(rentalStatus);
        } catch (Exception e) {
            log.error("Error in getRentalCountByStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRental(Long id) {

        log.info("Deleting rental with ID: " + id);

        try{

            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rental not found"));

            if (rental.getVehicles() != null) {
                Vehicle vehicle = rental.getVehicles();
                vehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
                vehicleRepository.save(vehicle);
            }

            paymentService.deletePayment(id);

            rentalRepository.deleteById(id);

            log.info("Rental deleted successfully!");

        } catch (Exception e) {
            log.error("rental delete is  error" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public long getTotalRentalCount() {
        log.info("Fetching total rental count");

        try{

            return rentalRepository.count();

        } catch (Exception e) {
            log.error("total count is error" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
