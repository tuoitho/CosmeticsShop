package com.cosmeticsellingwebsite.api;

import com.cosmeticsellingwebsite.payload.request.AddAddressRequest;
import com.cosmeticsellingwebsite.payload.request.UserRequest;
import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import com.cosmeticsellingwebsite.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserRC {
    @Autowired
    private UserService userService;
    //    add address
    @PostMapping("/address/add")
    public ResponseEntity<?> addAddress(@RequestBody AddAddressRequest addAddressRequest) {
        userService.addAddress(addAddressRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Add address successfully",null));
    }
    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses(@RequestParam Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Get addresses successfully", userService.getAddresses(userId)));
    }
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(new ApiResponse<>(true, "List users successfully", userService.list()));
    }
    @GetMapping("/find")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Find user successfully", userService.findById(id)));
    }
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Delete user successfully", null));
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Update user successfully", userService.update(userRequest)));
    }

}
