package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    void saveCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    CustomerDTO getCustomerById(Long id);

    List<CustomerDTO> getAllCustomers();

    List<CustomerDTO> searchCustomers(String keywords);
}
