package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.repository.AddressRepository;
import com.cosmeticsellingwebsite.repository.UserRepositoty;
import com.cosmeticsellingwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean updateAddressForUser(AddressForOrderDTO addressModel, Long addressID) {
        try {
            // Tìm AddressEntity trong database theo AddressID
            Address addressEntity = addressRepository.findById(addressID)
                    .orElse(new Address()); // Tạo mới nếu không tìm thấy

            // Cập nhật thông tin từ AddressModel vào AddressEntity
            addressEntity.setProvince(addressModel.getProvince());
            addressEntity.setDistrict(addressModel.getDistrict());
            addressEntity.setWard(addressModel.getWard());
            addressEntity.setAddress(addressModel.getAddress());
            addressEntity.setReceiverName(addressModel.getReceiverName());
            addressEntity.setReceiverPhone(addressModel.getReceiverPhone());

            // Lưu AddressEntity vào cơ sở dữ liệu
            addressRepository.save(addressEntity);

            return true; // Cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Cập nhật thất bại
        }
    }
    // Lấy danh sách địa chỉ của người dùng
    public List<AddressForOrderDTO> getAddressesForUser(Long userId) {
        List<Address> addresses = addressRepository.findByCustomer_UserId(userId);
        return addresses.stream().map(address -> new AddressForOrderDTO(
                address.getAddressId(),
                address.getReceiverName(),
                address.getReceiverPhone(),
                address.getAddress(),
                address.getProvince(),
                address.getDistrict(),
                address.getWard()
        )).toList();
    }

    // Lấy thông tin địa chỉ cụ thể
    public AddressForOrderDTO getAddressById(Long addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            return new AddressForOrderDTO(
                    address.getAddressId(),
                    address.getReceiverName(),
                    address.getReceiverPhone(),
                    address.getAddress(),
                    address.getProvince(),
                    address.getDistrict(),
                    address.getWard()
            );
        }
        return null;
    }

    // Lưu địa chỉ (thêm hoặc cập nhật)
    public boolean saveAddressForUser(AddressForOrderDTO addressDTO, Long userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                return false;
            }

            Address address = addressDTO.getAddressId() != null
                    ? addressRepository.findById(addressDTO.getAddressId()).orElse(new Address())
                    : new Address();

            address.setReceiverName(addressDTO.getReceiverName());
            address.setReceiverPhone(addressDTO.getReceiverPhone());
            address.setAddress(addressDTO.getAddress());
            address.setProvince(addressDTO.getProvince());
            address.setDistrict(addressDTO.getDistrict());
            address.setWard(addressDTO.getWard());
            address.setCustomer((Customer) userOpt.get());

            addressRepository.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id); // Gọi repository để xóa
    }

}
