package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/customer-management")
public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    // Hiển thị danh sách khách hàng
//    @GetMapping
//    public String listCustomers(Model model) {
//        List<Customer> customers = customerService.getAllCustomers();
//        model.addAttribute("customers", customers);
//        return "admin/admin-customer-list";
//    }
//
//    // Tìm kiếm khách hàng
//    @GetMapping("/search")
//    public String searchCustomers(@RequestParam("keyword") String keyword, Model model) {
//        List<Customer> customers = customerService.searchCustomers(keyword);
//        model.addAttribute("customers", customers);
//        return "admin/admin-customer-list";
//    }
//
//    // Xem chi tiết khách hàng
//    @GetMapping("/{id}")
//    public String getCustomerDetails(@PathVariable Long id, Model model) {
//        Customer customer = customerService.getCustomerById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
//        model.addAttribute("customer", customer);
//        return "admin/admin-customer-details";
//    }
//
//    // Khóa tài khoản
//    @PostMapping("/{id}/lock")
//    public String lockCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        try {
//            customerService.updateActiveStatus(id, false);
//            redirectAttributes.addFlashAttribute("successMessage", "Khóa tài khoản thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi khóa tài khoản: " + e.getMessage());
//        }
//        return "redirect:/admin/customer-management";
//    }
//
//    // Mở khóa tài khoản
//    @PostMapping("/{id}/unlock")
//    public String unlockCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        try {
//            customerService.updateActiveStatus(id, true);
//            redirectAttributes.addFlashAttribute("successMessage", "Mở khóa tài khoản thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi mở khóa tài khoản: " + e.getMessage());
//        }
//        return "redirect:/admin/customer-management";
//    }
}
