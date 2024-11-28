package com.cosmeticsellingwebsite.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterReq {
    @NotNull(message = "fullname is required")
    private String fullname;
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "username is required")
    private String username;
    @NotNull(message = "password is required")
    private String password;
}
