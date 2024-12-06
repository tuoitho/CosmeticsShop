package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Customer;

import java.util.List;
import java.util.Optional;


public interface ICustomerService {
    List<Customer> searchCustomers(String keyword);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    void updateActiveStatus(Long userId, boolean status);

    Optional<Customer> findById(Long id);


}
