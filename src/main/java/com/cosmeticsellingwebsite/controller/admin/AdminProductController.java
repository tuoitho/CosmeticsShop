package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductStock;
import com.cosmeticsellingwebsite.service.image.ImageService;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.interfaces.ICategoryService;
import com.cosmeticsellingwebsite.service.interfaces.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
//@RequestMapping({"/admin/products", "/manager/products"})
public class AdminProductController {
    @Autowired
    IProductService productService = new ProductService();

    @Autowired
    ICategoryService categoryService = new CategoryService();
    @Autowired
    private ImageService imageService;

    @GetMapping("")
    public String getAllProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "") String searchKeyword,
                                 @RequestParam(required = false) Boolean active,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productName").ascending());

        // Gọi service để lấy danh sách sản phẩm theo bộ lọc
        Page<Product> productPage = productService.searchAndFilterProducts(searchKeyword, active, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("active", active);

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
    public String saveProduct(@Valid @ModelAttribute Product product,
                              BindingResult result,
                              @RequestParam("imagePath") MultipartFile image,
                              @RequestParam("categoryId") Long categoryId,
                              RedirectAttributes redirectAttributes) throws IOException {
        // Kiểm tra xem productCode đã tồn tại trong cơ sở dữ liệu chưa
        if (productService.existsByProductCode(product.getProductCode())) {
            // Trả về thông báo lỗi nếu mã sản phẩm đã tồn tại
            return "redirect:/admin/products?error=duplicate_product_code";
        }

        // Kiểm tra ngày sản xuất và ngày hết hạn
        if (product.getManufactureDate().isBefore(product.getExpirationDate())) {
            redirectAttributes.addFlashAttribute("error", "Ngày sản xuất phải nhỏ hơn ngày hết hạn.");
            return "redirect:/admin/products/new";
        }

        // Kiểm tra mã sản phẩm đã tồn tại
        if (productService.existsByProductCode(product.getProductCode())) {
            redirectAttributes.addFlashAttribute("error", "Mã sản phẩm đã tồn tại.");
            return "redirect:/admin/products/new";
        }

        // Lấy danh mục
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Danh mục sản phẩm không hợp lệ.");
            return "redirect:/admin/products/new";
        }
        product.setCategory(category);


        // Cập nhật trạng thái và số lượng mặc định
        product.setActive(true);
        // Thiết lập quantity mặc định là 0
        ProductStock productStock = new ProductStock();
        productStock.setQuantity(0L); // Set quantity mặc định là 0
        productStock.setProduct(product); // Set quan hệ giữa Product và ProductStock

        // Lưu productStock
        product.setProductStock(productStock); // Liên kết Product với ProductStock

        //xử lý ảnh
        String imageUrl = imageService.saveImage(image);
        product.setImage(imageUrl);

        productService.addProduct(product);
        return "redirect:/admin/products";  // Chuyển hướng đến danh sách sản phẩm
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/disable/{id}")
    public String disableProduct(@PathVariable("id") Long id) {
        productService.disableProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/activate/{id}")
    public String activateProduct(@PathVariable("id") Long id) {
        productService.activateProduct(id);
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
    public String updateProduct(@ModelAttribute("product") Product product,@RequestParam("imagePath") MultipartFile image, Model model) throws IOException {
        // Kiểm tra ngày sản xuất và ngày hết hạn
        if (product.getManufactureDate().isAfter(product.getExpirationDate())) {
            model.addAttribute("error", "Ngày sản xuất phải nhỏ hơn ngày hết hạn!");
            return "admin/admin-product-edit"; // Quay lại trang chỉnh sửa
        }

        // Nếu người dùng không tải lên ảnh mới
        if (image == null || image.isEmpty()) {
            // Không thay đổi ảnh, giữ ảnh cũ
            String existingImage = productService.getExistingImage(product.getProductId());
            product.setImage(existingImage);
        } else {
            // Người dùng tải lên ảnh mới, lưu ảnh
            String newImageUrl = imageService.saveImage(image);
            product.setImage(newImageUrl);
        }
        productService.updateProduct(product);

        model.addAttribute("message", "Cập nhật thông tin sản phẩm thành công");
        return "redirect:/admin/products"; // Quay lại danh sách sản phẩm
    }



}
