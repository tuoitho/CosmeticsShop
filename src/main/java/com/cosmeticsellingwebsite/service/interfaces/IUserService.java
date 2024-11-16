package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.AddAddressRequest;
import com.cosmeticsellingwebsite.payload.request.RegisterRequest;
import com.cosmeticsellingwebsite.payload.request.UserRequest;
import com.cosmeticsellingwebsite.payload.response.UserResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IUserService {
    //    add address
    void addAddress(AddAddressRequest addAddressRequest);

    List<Address> getAddresses(Long userId);

    List<User> list();

    User findById(Long id);

    void delete(Long id);

    UserResponse update(UserRequest userRequest);

    void registerUser(@Valid RegisterRequest user);
}
