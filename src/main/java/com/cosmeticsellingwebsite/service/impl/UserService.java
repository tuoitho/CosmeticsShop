package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import com.cosmeticsellingwebsite.exception.CustomException;
import com.cosmeticsellingwebsite.payload.request.RegisterReq;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.security.UserPrincipal;
import com.cosmeticsellingwebsite.service.interfaces.IUserService;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;

    //    add address
//    public void addAddress(AddAddressRequest addAddressRequest) {
//        Optional<User> userOptional = userRepository.findById(addAddressRequest.getUserId());
//        if (userOptional.isEmpty()) {
//            throw new CustomException("User not found");
//        }
//
//        Address address = new Address();
//        BeanUtils.copyProperties(addAddressRequest, address);
//        address.setUser(userOptional.get());
//
//        addressRepository.save(address);
//    }

    public List<Address> getAddresses(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found");
        }
        return addressRepository.findAllByCustomer_UserId(userId);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CustomException("User " + id + " not found");
        }
        return userOptional.get();
    }

    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CustomException("User " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserName not found: " + username));
    }

    public void registerUser(@Valid RegisterReq registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException("Email already exists");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(registerRequest, customer);
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Role role = roleRepository.findByRoleName(RoleEnum.CUSTOMER).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setRoleName(RoleEnum.CUSTOMER);
            return roleRepository.save(newRole); // Lưu vào database
        });
        customer.setRole(role);
        Logger.log("Register: " + customer);
        userRepository.save(customer);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("Email not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void save(User user) {
        userRepository.save(user);
    }

}
