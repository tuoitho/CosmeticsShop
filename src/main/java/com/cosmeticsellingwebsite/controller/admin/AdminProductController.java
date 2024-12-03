package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.interfaces.ICategoryService;
import com.cosmeticsellingwebsite.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    IProductService productService = new ProductService();

    @Autowired
    ICategoryService categoryService = new CategoryService();

    @GetMapping("")
    public String getAllProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
        Page<Product> productPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admin-product-list";
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/new")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategoriess()); // Truyền danh mục vào view
        model.addAttribute("product", new Product()); // Truyền đối tượng Product mới để liên kết với form
        return "admin/admin-product-add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("categoryId") Long categoryId) {
        // Kiểm tra xem productCode đã tồn tại trong cơ sở dữ liệu chưa
        if (productService.existsByProductCode(product.getProductCode())) {
            // Trả về thông báo lỗi nếu mã sản phẩm đã tồn tại
            return "redirect:/admin/products?error=duplicate_product_code";
        }

        // Lấy đối tượng Category từ categoryId
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            product.setCategory(category);
        }


        // Cập nhật trạng thái và số lượng mặc định
        product.setActive(true);
        // Thiết lập quantity mặc định là 0
        ProductStock productStock = new ProductStock();
        productStock.setQuantity(0L); // Set quantity mặc định là 0
        productStock.setProduct(product); // Set quan hệ giữa Product và ProductStock

        // Lưu productStock
        product.setProductStock(productStock); // Liên kết Product với ProductStock

        productService.addProduct(product);
        return "redirect:/admin/products";  // Chuyển hướng đến danh sách sản phẩm
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    // Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/admin-product-edit";
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product) {
        productService.updateProduct(product);
        return "redirect:/admin/products"; // Quay lại danh sách sản phẩm
    }



}
