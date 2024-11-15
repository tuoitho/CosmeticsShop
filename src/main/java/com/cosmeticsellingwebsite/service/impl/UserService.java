package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.entity.Employee;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.AddAddressRequest;
import com.cosmeticsellingwebsite.payload.request.UserRequest;
import com.cosmeticsellingwebsite.payload.response.UserResponse;
import com.cosmeticsellingwebsite.repository.AddressRepository;
import com.cosmeticsellingwebsite.repository.CustomerRepository;
import com.cosmeticsellingwebsite.repository.EmployeeRepository;
import com.cosmeticsellingwebsite.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    //    add address
    public void addAddress(AddAddressRequest addAddressRequest) {
        Optional<User> userOptional = userRepository.findById(addAddressRequest.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Address address = new Address();
        BeanUtils.copyProperties(addAddressRequest, address);
        address.setUser(userOptional.get());

        addressRepository.save(address);

//        User user = userRepository.findById(addAddressRequest.getUserId()).get();
//        Customer customer=customerRepository.findById(addAddressRequest.getUserId()).get();
//        Address address = new Address();
//        BeanUtils.copyProperties(addAddressRequest, address);
//        address.setCustomer(customer);
//        Set<Address> addresses = customer.getAddresses();
//        addresses.add(address);
//        customer.setAddresses(addresses);
//        customerRepository.save(customer);
//        return customerRepository.save(customer);
    }

    public List<Address> getAddresses(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return addressRepository.findAllByUser_UserId(userId);
    }
    public List<User> list() {
        return userRepository.findAll();
    }
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOptional.get();
    }

    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
    public UserResponse update(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findById(userRequest.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
//        neu user la customer
        if (user instanceof Customer customer) {
            BeanUtils.copyProperties(userRequest, customer);
            Customer cs=customerRepository.save(customer);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(cs, userResponse);
            return userResponse;
        }else if (user instanceof Employee employee) {
            // TODO
            BeanUtils.copyProperties(userRequest, employee);
            Employee emp=  employeeRepository.save(employee);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(emp, userResponse);
            return userResponse;
        }
        return null;
    }

}
