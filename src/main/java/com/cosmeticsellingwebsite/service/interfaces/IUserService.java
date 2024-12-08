package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.dto.AddUserDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.payload.request.RegisterReq;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IUserService {
    List<Address> getAddresses(Long userId);

    List<User> list();

    User findById(Long id);

    void delete(Long id);

    void registerUser(@Valid RegisterReq registerRequest);

    User findByEmail(String email);

    void resetPassword(String email, String newPassword);

    List<User> findAll();

    void save(User user);

    void saveUser(AddUserDTO user);

    void createUser(AddUserDTO user);

    List<User> searchUsers(String keyword);

    Page<User> searchUsers(String keyword, int page, int pageSize);

    Page<User> getUsers(int page, int pageSize);
}
