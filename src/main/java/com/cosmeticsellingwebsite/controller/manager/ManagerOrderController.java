package com.cosmeticsellingwebsite.controller.manager;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class ManagerOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size,
                             @RequestParam(value = "search", required = false) String searchKeyword,
                             @RequestParam(value = "status", required = false) OrderStatus selectedStatus,
                             Model model) {

        Page<Order> orders = orderService.getPaginatedOrders(page, size, searchKeyword, selectedStatus);

        model.addAttribute("orders", orders);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("selectedStatus", selectedStatus);

        return "manager/order-list";
    }


    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus newStatus,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/{id}/details")
    public String viewOrderDetails(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "manager/order-details";
    }


}
