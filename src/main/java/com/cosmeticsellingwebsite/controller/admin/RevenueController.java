package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.service.impl.RevenueService;
import com.cosmeticsellingwebsite.service.interfaces.IRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {
    @Autowired
    IRevenueService revenueService = new RevenueService();
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayRevenue() {
        double revenue = revenueService.calculateDailyRevenue();
        System.out.println("day " + revenue);
        Map<String, Object> response = new HashMap<>();
        response.put("revenue", revenue);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyRevenue() {
        double revenue = revenueService.calculateMonthlyRevenue();
        System.out.println("month " + revenue);
        Map<String, Object> response = new HashMap<>();
        response.put("revenue", revenue);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/annual")
    public ResponseEntity<Map<String, Object>> getAnnualRevenue() {
        double revenue =  revenueService.calculateYearlyRevenue();
        System.out.println("year " + revenue);
        Map<String, Object> response = new HashMap<>();
        response.put("revenue", revenue);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/annual-data")
    public ResponseEntity<Map<String, Object>> getAnnualData() {
        double[] monthlyRevenue = revenueService.calculateMonthlyRevenueForYear();
        Map<String, Object> response = new HashMap<>();
        response.put("monthlyRevenue", monthlyRevenue);
        return ResponseEntity.ok(response);
    }
}