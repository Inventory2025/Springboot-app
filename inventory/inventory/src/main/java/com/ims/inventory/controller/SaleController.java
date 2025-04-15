package com.ims.inventory.controller;

import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.service.impl.SaleMasterServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/sales/")
public class SaleController {

    private final SaleMasterServiceImpl saleService;

    public SaleController(SaleMasterServiceImpl saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleTrans> create(@RequestBody SaleRequest dto) {
        return ResponseEntity.ok(saleService.createSale(dto));
    }

    @GetMapping
    public List<SaleTrans> getAll() {
        return saleService.getAllSales();
    }
}
