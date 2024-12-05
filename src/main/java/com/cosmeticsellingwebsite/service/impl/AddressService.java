package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;
import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.repository.AddressRepository;
import com.cosmeticsellingwebsite.repository.UserRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
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
}
