package com.cosmeticsellingwebsite.dto;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.enums.Gender;
import lombok.Data;

@Data
public class AddUserDTO {
    protected Long userId;
    protected String username;
    protected String password;
    protected String email;
    protected String fullname;
    protected String phone;
    protected Gender gender;
    protected String image;
    protected Boolean active;
    protected Role role;
}
