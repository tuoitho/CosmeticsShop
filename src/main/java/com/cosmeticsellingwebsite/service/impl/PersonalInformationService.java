package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.*;
import com.cosmeticsellingwebsite.entity.*;
import com.cosmeticsellingwebsite.repository.*;
import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.dto.UserDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import com.cosmeticsellingwebsite.repository.AddressRepository;
import com.cosmeticsellingwebsite.repository.RoleRepository;
import com.cosmeticsellingwebsite.service.interfaces.IPersonalInformationService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalInformationService implements IPersonalInformationService {
    @Autowired
    private UserRepositoty userRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public UserDTO fetchPersonalInfo(Long userID) {
        Optional<User> userEntityOpt = userRepository.findById(userID);
        if (userEntityOpt.isPresent()) {
            Customer userEntity = (Customer)userEntityOpt.get();

            // Chuyển đổi địa chỉ sang DTO
            List<AddressForOrderDTO> addressDTOs = userEntity.getAddresses().stream()
                    .map(address -> new AddressForOrderDTO(
                            address.getAddressId(),
                            address.getReceiverName(),
                            address.getReceiverPhone(),
                            address.getAddress(),
                            address.getProvince(),
                            address.getDistrict(),
                            address.getWard()))
                    .collect(Collectors.toList());

            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getUserId());
            userDTO.setName(userEntity.getFullname());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setPassword(userEntity.getPassword());
            userDTO.setGender(userEntity.getGender());
            userDTO.setPhone(userEntity.getPhone());
            userDTO.setRole(userEntity.getRole());
            userDTO.setAddresses(addressDTOs); // Gán danh sách địa chỉ
            userDTO.setImage(userEntity.getImage());
            userDTO.setBirthDate(userEntity.getBirthDate());

            return userDTO;
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

            Optional<User> existingUserOpt = userRepository.findById(userID);
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
            Role role = roleRepository.findByRoleName(RoleEnum.CUSTOMER).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setRoleName(RoleEnum.CUSTOMER);
                return roleRepository.save(newRole);
            });
            existingUser.setRole(userDTO.getRole() != null ? userDTO.getRole() : role);
            if (userDTO.getImage() != null && !userDTO.getImage().isEmpty()) {
                existingUser.setImage(userDTO.getImage());
            }
            existingUser.setBirthDate(userDTO.getBirthDate());



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

            userRepository.save(existingUser); // Lưu hoặc cập nhật user
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> findUserById(Long userID) {
        return userRepository.findById(userID);
    }

    @Override
    public boolean updatePassword(Long userId, String encodedPassword) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setPassword(encodedPassword);
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
