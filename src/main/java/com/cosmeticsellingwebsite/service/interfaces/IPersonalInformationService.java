package com.cosmeticsellingwebsite.service.interfaces;

import com.cosmeticsellingwebsite.dto.UserDTO;
import com.cosmeticsellingwebsite.entity.User;

import java.util.Optional;

public interface IPersonalInformationService {
    UserDTO fetchPersonalInfo(Long userID);
    boolean savePersonalInfo(UserDTO userModel, Long userID);
    Optional<User> findUserById(Long userID);
}
