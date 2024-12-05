package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.*;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.service.interfaces.IPersonalInformationService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalInformationService implements IPersonalInformationService {
    @Autowired
    private UserRepositoty userRepositoty;

    @Autowired
    private AddressRepository addressRepository;

    // Lấy thông tin cá nhân từ cơ sở dữ liệu
    @Override
    public UserDTO fetchPersonalInfo(Long userID) {
        Optional<User> userEntityOpt = userRepositoty.findById(userID);
        if (userEntityOpt.isPresent()) {
            Customer userEntity = (Customer) userEntityOpt.get();
            Address addressEntity = userEntity.getAddresses().stream().findFirst().orElse(null);
            Logger.log("Address: " + addressEntity);
            AddressForOrderDTO addressDTO = null;
            if (addressEntity != null) {
                addressDTO = new AddressForOrderDTO(
                        addressEntity.getAddressId(),
                        addressEntity.getReceiverName(),
                        addressEntity.getReceiverPhone(),
                        addressEntity.getAddress(),
                        addressEntity.getProvince(),
                        addressEntity.getDistrict(),
                        addressEntity.getWard()
                );
            }

            return new UserDTO(
                    userEntity.getUserId(),
                    userEntity.getFullname(),
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    userEntity.getGender(),
                    userEntity.getPhone(),
                    userEntity.getRole(),
                    addressDTO
            );
        }
        return null;
    }

    // Lưu thông tin cá nhân vào cơ sở dữ liệu
    @Override
    public boolean savePersonalInfo(UserDTO userDTO, Long userID) {
        try {
            if (userID == null) {
                return false; // Không thể lưu nếu userID không tồn tại
            }

            Optional<User> existingUserOpt = userRepositoty.findById(userID);
            if (!existingUserOpt.isPresent()) {
                return false; // Không thể lưu nếu user không tồn tại
            }

            Customer existingUser = (Customer)existingUserOpt.get();

            // Cập nhật thông tin user
            existingUser.setFullname(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setPassword(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                    ? userDTO.getPassword()
                    : existingUser.getPassword());
            existingUser.setGender(userDTO.getGender());
            existingUser.setRole(userDTO.getRole());

            // Xử lý thông tin địa chỉ
            AddressForOrderDTO addressDTO = userDTO.getAddress();
            if (addressDTO != null) {
                Address addressEntity = addressDTO.getAddressId() != null
                        ? addressRepository.findById(addressDTO.getAddressId()).orElse(new Address())
                        : new Address();

                addressEntity.setReceiverName(addressDTO.getReceiverName());
                addressEntity.setReceiverPhone(addressDTO.getReceiverPhone());
                addressEntity.setAddress(addressDTO.getAddress());
                addressEntity.setProvince(addressDTO.getProvince());
                addressEntity.setDistrict(addressDTO.getDistrict());
                addressEntity.setWard(addressDTO.getWard());
                addressEntity.setCustomer(existingUser); // Liên kết với User hiện tại

                addressRepository.save(addressEntity); // Lưu địa chỉ mới hoặc cập nhật
            }

            userRepositoty.save(existingUser); // Lưu hoặc cập nhật user
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> findUserById(Long userID) {
        return userRepositoty.findById(userID);
    }
}
