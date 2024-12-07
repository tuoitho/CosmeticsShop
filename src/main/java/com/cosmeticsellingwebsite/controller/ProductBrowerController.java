package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.dto.ProductSearchDTO;
import com.cosmeticsellingwebsite.dto.VoucherDTO;
import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.Voucher;
import com.cosmeticsellingwebsite.payload.response.ProductDetailResponse;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.impl.VoucherService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/browser")
public class ProductBrowerController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VoucherService voucherService;


    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(value = "keyword", required = false,defaultValue = "") String keyword,
            @RequestParam(value = "minPrice", required = false,defaultValue = "0") Double minPrice,
            @RequestParam(value = "maxPrice", required = false,defaultValue = "9999999999") Double maxPrice,
            @RequestParam(value = "brand", required = false,defaultValue = "") String brand,
            @RequestParam(value = "origin", required = false,defaultValue = "") String origin,
            @RequestParam(value = "category", required = false,defaultValue = "") String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ){
        if (keyword == null) {
            keyword = ""; }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("productName").ascending());

        Logger.log("Searching products with keyword: " + keyword + ", minPrice: " + minPrice + ", maxPrice: " + maxPrice + ", brand: " + brand + ", origin: " + origin + ", category: " + category);
        Page<ProductSearchDTO> products = productService.searchProducts(keyword, minPrice, maxPrice, brand, origin, category, pageable);
        Logger.log("Products: " + products.getContent());
        model.addAttribute("products", products.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("brand", brand);
        model.addAttribute("origin", origin);
        model.addAttribute("category", category);

        List<String> categories = categoryService.getAllCategoriess().stream().map(Category::getCategoryName).toList();
        model.addAttribute("categories", categories);
        return "user/searchProduct";
    }



    @GetMapping("/category/products")
    public ResponseEntity<?> getCategoryWithProducts(@RequestParam Long categoryId, @RequestParam(required = false) Integer page) {
        if (page == null) {
            return ResponseEntity.ok(categoryService.getCategoryWithProducts(categoryId));
        }
        int totalProducts = categoryService.countProducts(categoryId);
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int pageNumber = page; // trang đầu tiên
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").ascending());
        return ResponseEntity.ok(categoryService.getCategoryWithProductsPaging(categoryId, pageable));
    }
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriess());
    }
    @GetMapping("/products")
    public String browseProducts() {
//        Logger.log("Browsing products");
        return "user/products";
    }
//    @GetMapping("/product/{productCode}")
//    public String productDetail(@PathVariable("productCode") String productCode, ModelMap model) {
//        model.addAttribute("productCode", productCode); // Thêm sản phẩm vào model
//        return "user/product"; // Trả về view product-detail
//    }
    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable("productId") Long productId, ModelMap model) {
        ProductDetailResponse productDetailResponse = productService.getProductDetailById(productId);
        model.addAttribute("productId", productId); // Thêm sản phẩm vào model
        model.addAttribute("product", productDetailResponse);
        Logger.log("Product detail: " + productDetailResponse);
        return "user/product-detail"; // Trả về view product-detail
    }



    @GetMapping("/vouchers")
    public String ShowVoucher() {
        return "user/voucher";
    }

    @GetMapping("/getVouchers")
    @ResponseBody
    public List<VoucherDTO> getVouchers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "8") int size) {
        Page<Voucher> voucherPage = voucherService.getVouchersWithPagination(page, size);
        Page<VoucherDTO> voucherDTOPage = voucherPage.map(voucher -> {
            VoucherDTO voucherDTO = new VoucherDTO();
            BeanUtils.copyProperties(voucher, voucherDTO);
            voucherDTO.setQuantity(voucherService.countByVoucherCode(voucher.getVoucherCode()));
            voucherDTO.setQuantityUsed(voucherService.countByUsedTrueAndVoucherCode(voucher.getVoucherCode()));
            voucherDTO.setQuantityAvailable(voucherService.countByUsedFalseAndVoucherCode(voucher.getVoucherCode()));
            return voucherDTO;
        });
        return voucherDTOPage.getContent();
    }
}
