package com.cosmeticsellingwebsite.controller;

import com.cosmeticsellingwebsite.config.AuthenticationHelper;
import com.cosmeticsellingwebsite.dto.ProductHomeDTO;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.security.UserPrincipal;
import com.cosmeticsellingwebsite.security.oauth.CustomOAuth2User;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import com.cosmeticsellingwebsite.util.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private AuthenticationHelper authenticationHelper;
    @Autowired
    private ProductService productService;
    @GetMapping("/")
    public String home(Model model) {
//        model.addAttribute("top20BestSellingProducts", productService.getTop20BestSellingProducts());
//        model.addAttribute("top20NewestProducts", productService.getTop20NewestProducts());
//        model.addAttribute("top20HighestRatingProducts", productService.getTop20HighestRatedProducts());
        return "user/homenew";
    }

    @GetMapping("/topSellingProducts")
    public ResponseEntity<?> topSellingProducts(@RequestParam(value = "page",defaultValue = "0") int page,
                                                @RequestParam(value = "size",defaultValue = "5") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Product> products = productService.getTop20BestSellingProductsPage(pageable);
        List<Product> content = products.getContent();
        List<ProductHomeDTO> productHomeDTOS = content.stream().map(x->{
            ProductHomeDTO productHomeDTO = new ProductHomeDTO();
            BeanUtils.copyProperties(x, productHomeDTO);
            productHomeDTO.setTotalSoldLast30Days(productService.countSoldLast30DaysByProductId(x.getProductId()));
            productHomeDTO.setAverageRating(productService.getAverageRatingByProductId(x.getProductId()));
            return productHomeDTO;
        }).toList();
        return ResponseEntity.ok(productHomeDTOS);
    }
    @GetMapping("/topNewestProducts")
    public ResponseEntity<?> topNewestProducts(@RequestParam(value = "page",defaultValue = "0") int page,
                                               @RequestParam(value = "size",defaultValue = "5") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Product> products = productService.getTop20NewestProductsPage(pageable);
        List<Product> content = products.getContent();
        List<ProductHomeDTO> productHomeDTOS = content.stream().map(x->{
            ProductHomeDTO productHomeDTO = new ProductHomeDTO();
            BeanUtils.copyProperties(x, productHomeDTO);
            productHomeDTO.setTotalSoldLast30Days(productService.countSoldLast30DaysByProductId(x.getProductId()));
            productHomeDTO.setAverageRating(productService.getAverageRatingByProductId(x.getProductId()));
            return productHomeDTO;
        }).toList();
        return ResponseEntity.ok(productHomeDTOS);
    }
    @GetMapping("/topHighestRatingProducts")
    public ResponseEntity<?> topHighestRatingProducts(@RequestParam(value = "page",defaultValue = "0") int page,
                                                      @RequestParam(value = "size",defaultValue = "5") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Product> products = productService.getTop20HighestRatedProductsPage(pageable);
        List<Product> content = products.getContent();
        List<ProductHomeDTO> productHomeDTOS = content.stream().map(x->{
            ProductHomeDTO productHomeDTO = new ProductHomeDTO();
            BeanUtils.copyProperties(x, productHomeDTO);
            productHomeDTO.setTotalSoldLast30Days(productService.countSoldLast30DaysByProductId(x.getProductId()));
            productHomeDTO.setAverageRating(productService.getAverageRatingByProductId(x.getProductId()));
            return productHomeDTO;
        }).toList();
        return ResponseEntity.ok(productHomeDTOS);
    }




}
