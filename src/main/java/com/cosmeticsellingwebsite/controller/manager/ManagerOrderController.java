package com.cosmeticsellingwebsite.controller.manager;

import com.cosmeticsellingwebsite.dto.OrderHistoryDetailDTO;
import com.cosmeticsellingwebsite.dto.ProductSnapshotDTO;
import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import com.cosmeticsellingwebsite.util.Logger;
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
//                             @RequestParam(value = "status", required = false) OrderStatus selectedStatus,
                             @RequestParam(value = "status", required = false) String selectedStatus,
                             Model model) {

        Logger.log("Searching orders with keyword: ");
        Page<Order> orders = orderService.getPaginatedOrders(page, size, searchKeyword, (selectedStatus!=null&&selectedStatus!="")?OrderStatus.valueOf(selectedStatus):null);
        Logger.log("Orders: " + orders.getContent());
        model.addAttribute("orders", orders);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("selectedStatus", selectedStatus);

        return "manager/order-list";
    }


    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus newStatus,
                                    @RequestParam(required = false) String content,
                                    RedirectAttributes redirectAttributes) {
        try {
//            orderService.updateOrderStatus(id, newStatus);
            orderService.updateOrderStatusWithContent(id, newStatus,content);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/{id}/details")
    public String viewOrderDetails(@PathVariable("id") Long orderId, Model model) {
        OrderHistoryDetailDTO order = orderService.getOrderHistoryDetailById(orderId);
        model.addAttribute("orderDetail", order);
        return "manager/order-details";
    }


    @GetMapping("/{orderId}/product-detail-snapshot/{productId}")
    public String productDetailSnapshot(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId, Model model) {
        ProductSnapshotDTO productSnapshot = orderService.getProductSnapshot(orderId, productId);
        model.addAttribute("productSnapshot", productSnapshot);
        return "customer/product-snapshot";
    }

}
