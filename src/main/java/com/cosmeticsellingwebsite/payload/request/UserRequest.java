package com.cosmeticsellingwebsite.payload.request;

import com.cosmeticsellingwebsite.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate startDate;


}
