package com.cosmeticsellingwebsite.config;

import com.cosmeticsellingwebsite.entity.Order;
import com.cosmeticsellingwebsite.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/test")
@RestController
public class TESTDATA {
    @Autowired
    OrderRepository orderRepository;
    @GetMapping("")
    public Object test() {
        Pageable pageable = PageRequest.of(2, 10);
        Page<Order> orders = orderRepository.searchOrdersByCustomerIdAndProductName(1L, "Tay", pageable);
        if (orders==null) return "No orders found";
        return orders.getContent();


    }
}
