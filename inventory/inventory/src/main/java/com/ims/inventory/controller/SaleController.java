package com.ims.inventory.controller;

import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.service.impl.SaleMasterServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/sales/")
public class SaleController {

    @Autowired
    private SaleMasterServiceImpl saleService;

    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody SaleRequest dto, HttpServletRequest request) {
        return ResponseEntity.ok(saleService.save(dto, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> softDelete(@PathVariable String id) {
        saleService.softDelete(id);
        return ResponseEntity.ok("Sale deactivated.");
    }

    @GetMapping
    public ResponseEntity<?> getAllActive() {
        return ResponseEntity.ok(saleService.getAll());
    }
}
