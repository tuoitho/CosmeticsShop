package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.enums.Gender;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userID;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String phone;
    private Role role;
    private AddressForOrderDTO address;
}