package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    void updateActiveStatus(Long userId, boolean status);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    List<Customer> searchByKeyword(String keyword);

    void lockAccount(Long customerId);

    void unlockAccount(Long customerId);

    void toggleActiveStatus(Long customerId);

    void deleteCustomerById(Long customerId);

    Page<Customer> findAll(Pageable pageable);
}
