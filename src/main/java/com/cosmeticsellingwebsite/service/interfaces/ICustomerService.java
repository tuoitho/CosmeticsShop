package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    public List<Customer> searchCustomers(String keyword);
    public List<Customer> getAllCustomers();
    public Optional<Customer> getCustomerById(Long id);
    public void updateActiveStatus(Long userId, boolean status);
}
