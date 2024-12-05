package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.ProductStock;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.impl.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/stock")
public class AdminProductStockController {
    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProductStocks(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ProductStock> productStockPage = productStockService.getPaginatedProductStocks(pageable);
        model.addAttribute("productStockPage", productStockPage);
        return "admin/admin-stock-list"; // View hiển thị danh sách kho
    }

    @PostMapping("/update/{productId}")
    public String updateProductStock(
            @PathVariable Long productId,
            @RequestParam Long addedQuantity,
            RedirectAttributes redirectAttributes, Model model) {
        if (addedQuantity < 0) {
            model.addAttribute("error", "Số lượng thêm vào phải là số không âm.");
            return "redirect:/admin/stock";
        }
        productStockService.updateProductStock(productId, addedQuantity);
        redirectAttributes.addFlashAttribute("message", "Cập nhật số lượng thành công.");
        return "redirect:/admin/stock";
    }

}
