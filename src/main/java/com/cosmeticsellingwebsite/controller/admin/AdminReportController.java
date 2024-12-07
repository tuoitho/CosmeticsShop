package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.enums.OrderStatus;
import com.cosmeticsellingwebsite.payload.response.CategorySalesResp;
import com.cosmeticsellingwebsite.service.impl.CartService;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import com.cosmeticsellingwebsite.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@RequestMapping({"/admin/report","/admin"})
@Controller
public class AdminReportController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;


    @GetMapping("")
    public String getReport(Model model) {
        List<Category> categories = categoryService.getAllCategoriess();
        model.addAttribute("categories", categories);
        List<Order> top5OrdersRecently = orderService.getTop5OrdersRecently();
        model.addAttribute("top5OrdersRecently", top5OrdersRecently);
        Long countPendingOrders = orderService.countPendingOrders();
        model.addAttribute("countPendingOrders", countPendingOrders);
        Long cntProductInCart = cartService.countProductInCart();
        model.addAttribute("cntProductInCart", cntProductInCart);
        return "admin/admin-report";
    }


    @GetMapping("/category")
    @ResponseBody
    public String getStringArrayCategory(Model model) {
        List<Category> categories = categoryService.getAllCategoriess();
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getCategoryName();
        }
        return Arrays.toString(categoryNames);
    }
    @GetMapping("category-total-sold")
    @ResponseBody
    public List<CategorySalesResp> getCategorySales(Model model) {
        List<CategorySalesResp> categoryTotalSold = categoryService.getCategoryTotalSold();
        //tinh phan tram
        long total = categoryTotalSold.stream().mapToLong(CategorySalesResp::getTotalSold).sum();
        for (CategorySalesResp categorySale : categoryTotalSold) {
            categorySale.setPercentage((double) categorySale.getTotalSold() / total * 100);
        }
        return categoryTotalSold;
    }
}
