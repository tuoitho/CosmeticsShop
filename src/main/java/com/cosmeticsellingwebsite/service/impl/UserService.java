package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.AddUserDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public List<Address> getAddresses(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found");
        }
        return addressRepository.findByCustomer_UserId(userId);
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CustomException("User " + id + " not found");
        }
        return userOptional.get();
    }

    @Override
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

    @Override
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

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException("Email not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveUser(AddUserDTO user) {
        User userEntity = userRepository.findById(user.getUserId()).orElseThrow(() -> new CustomException("User not found"));
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
    }
    @Override
    public void createUser(AddUserDTO user) {
        User userEntity = new User();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
    }


    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.findByFullnameContainingOrUsernameContainingOrEmailContaining(keyword, keyword, keyword);
    }
    @Override
    public Page<User> searchUsers(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findByRole_RoleNameInAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                List.of(RoleEnum.MANAGER, RoleEnum.CUSTOMER), keyword, keyword, pageable);
    }

    @Override
    public Page<User> getUsers(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findByRole_RoleNameIn(List.of(RoleEnum.MANAGER, RoleEnum.CUSTOMER), pageable);
    }

    public String getExistingImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return user.getImage();
    }

}
