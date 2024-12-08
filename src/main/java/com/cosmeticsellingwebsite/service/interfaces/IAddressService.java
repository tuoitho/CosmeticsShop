package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.dto.AddressForOrderDTO;

import java.util.List;

public interface IAddressService {
    boolean updateAddressForUser(AddressForOrderDTO addressModel, Long addressID);

    // Lấy danh sách địa chỉ của người dùng
    List<AddressForOrderDTO> getAddressesForUser(Long userId);

    // Lấy thông tin địa chỉ cụ thể
    AddressForOrderDTO getAddressById(Long addressId);

    // Lưu địa chỉ (thêm hoặc cập nhật)
    boolean saveAddressForUser(AddressForOrderDTO addressDTO, Long userId);

    void deleteAddressById(Long id);

    boolean checkAddressBelongToUser(Long id, Long userID);
}
