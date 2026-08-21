package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.CustomerDTO;
import lk.ijse.MainCabService.entity.Customer;
import lk.ijse.MainCabService.entity.Vehicle;
import lk.ijse.MainCabService.repository.CustomerRepository;
import lk.ijse.MainCabService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        log.info("Executing save Customer()");

        try{

            Customer customer = new Customer();

            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setCustomerNumber(customerDTO.getCustomerNumber());
            customer.setCustomerNIC(customerDTO.getCustomerNIC());
            customer.setCustomerLicenseNumber(customerDTO.getCustomerLicenseNumber());
            customer.setCustomerAddress(customerDTO.getCustomerAddress());
            customer.setCustomerRegisterDate(LocalDate.now());

            customerRepository.save(customer);
            log.info("customer saved successfully!");

        } catch (Exception e) {
            log.error("Error in save Customer(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {

        log.info("Executing update Customer()");

        try{
            Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getCustomerID());
            if(!optionalCustomer.isPresent()){
                throw new RuntimeException("customer not found");
            }

            Customer customer = optionalCustomer.get();

            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setCustomerNumber(customerDTO.getCustomerNumber());
            customer.setCustomerNIC(customerDTO.getCustomerNIC());
            customer.setCustomerLicenseNumber(customerDTO.getCustomerLicenseNumber());
            customer.setCustomerAddress(customerDTO.getCustomerAddress());

            customerRepository.save(customer);
            log.info("customer updated successfully!");


        } catch (Exception e) {
            log.error("Error in update Customer(): " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteCustomer(Long id) {

        log.info("executing delete customer!");

        try {

            Optional<Customer> customerOptional = customerRepository.findById(id);

            if(!customerOptional.isPresent()){
                throw new RuntimeException("customer not found with ID" + id);
            }

            customerRepository.deleteById(id);


            log.info("customer deleted successfully for ID: " + id);

        } catch (Exception e) {
            log.error("error in update customer(): " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @Override
    public CustomerDTO getCustomerById(Long id) {

        log.info("executing customer details!");

        try {

            Optional<Customer>  optionalCustomer = customerRepository.findById(id);

            if (!optionalCustomer.isPresent()){
                throw new RuntimeException("customer not found with ID" + id);
            }

            Customer customer = optionalCustomer.get();

            CustomerDTO customerDTO = new CustomerDTO();

            customerDTO.setCustomerID(customer.getCustomerID());
            customerDTO.setCustomerName(customer.getCustomerName());
            customerDTO.setCustomerNumber(customer.getCustomerNumber());
            customerDTO.setCustomerNIC(customer.getCustomerNIC());
            customerDTO.setCustomerLicenseNumber(customer.getCustomerLicenseNumber());
            customerDTO.setCustomerAddress(customer.getCustomerAddress());

            log.info("add" + customerDTO + " vehicle details successfully.");
            return customerDTO;

        } catch (Exception e) {
            log.error("error in customer details(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Executing getAllCustomers()");

        try {

            List<Customer> customerList = customerRepository.findAll();
            List<CustomerDTO> customerDTOList = new ArrayList<>();

            for(Customer customer : customerList) {

                CustomerDTO customerDTO =new CustomerDTO();

                customerDTO.setCustomerID(customer.getCustomerID());
                customerDTO.setCustomerName(customer.getCustomerName());
                customerDTO.setCustomerNumber(customer.getCustomerNumber());
                customerDTO.setCustomerNIC(customer.getCustomerNIC());
                customerDTO.setCustomerLicenseNumber(customer.getCustomerLicenseNumber());
                customerDTO.setCustomerAddress(customer.getCustomerAddress());
                customerDTO.setCustomerRegisterDate(customer.getCustomerRegisterDate());

                customerDTOList.add(customerDTO);
            }

            log.info("add" + customerDTOList.size() + " customers successfully.");
            return customerDTOList;

        } catch (Exception e) {
            log.error("error in get AllCustomers(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Executing searchCustomers with keyword: " + keyword);

        try {
            List<Customer> customerList = customerRepository.searchCustomers(keyword);
            List<CustomerDTO> customerDTOList = new ArrayList<>();

            for (Customer customer : customerList) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerID(customer.getCustomerID());
                customerDTO.setCustomerName(customer.getCustomerName());
                customerDTO.setCustomerNumber(customer.getCustomerNumber());
                customerDTO.setCustomerNIC(customer.getCustomerNIC());
                customerDTO.setCustomerLicenseNumber(customer.getCustomerLicenseNumber());
                customerDTO.setCustomerAddress(customer.getCustomerAddress());
                customerDTO.setCustomerRegisterDate(customer.getCustomerRegisterDate());

                customerDTOList.add(customerDTO);
            }

            return customerDTOList;

        } catch (Exception e) {
            log.error("Error in searchCustomers(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
