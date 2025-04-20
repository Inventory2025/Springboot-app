package com.ims.inventory.controller;

import com.ims.inventory.service.impl.DashboardServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardServiceImpl dashboardService;

    @GetMapping("resent/sales")
    public ResponseEntity<?> getResentSales(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.resentSales(request));
    }

    @GetMapping("cards")
    public ResponseEntity<?> getCards(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.getCards(request));
    }

    @GetMapping("stock/alert")
    public ResponseEntity<?> getStockAlert(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.resentStockAlert(request));
    }

    @GetMapping("selling/product")
    public ResponseEntity<?> getSellingProduct(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.getSellingProducts(request));
    }

    @GetMapping("sell_purchase")
    public ResponseEntity<?> getSellAndPurchase(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.getSellAndPurchase(request));
    }

    @GetMapping("top_customer")
    public ResponseEntity<?> getTopCustomer(HttpServletRequest request) {
        return ResponseEntity.ok(dashboardService.getTopCustomer(request));
    }


}
