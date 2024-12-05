package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.repository.CustomerRepository;
import com.cosmeticsellingwebsite.repository.UserRepository;
import com.cosmeticsellingwebsite.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return customerRepository.findAllCustomers();
        }
        return customerRepository.findByFullnameOrEmail(keyword, keyword);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void updateActiveStatus(Long userId, boolean status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + userId));
        user.setActive(status);
        userRepository.save(user); // Lưu thay đổi
    }
}
