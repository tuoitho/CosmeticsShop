package com.cosmeticsellingwebsite.config;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.repository.OrderRepository;
import com.cosmeticsellingwebsite.repository.ProductRepository;
import com.cosmeticsellingwebsite.util.Logger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RequestMapping("/test")
@RestController
public class TESTDATA {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @GetMapping
    public String t(Principal principal) {
       Logger.log(principal);
        Authentication authentication = (Authentication) principal;
        Logger.log(authentication);
        Logger.log(authentication.getPrincipal());
        Logger.log(authentication.getAuthorities());
        return "TESTDATA";


    }
}
