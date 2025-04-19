package com.ims.inventory.controller;

import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.PurchaseRequest;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.service.impl.PurchaseServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/purchase/")
public class PurchaseController {

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest dto, HttpServletRequest request) throws ImsBusinessException {
        return ResponseEntity.ok(purchaseService.save(dto, request));
    }

    @PostMapping("load")
    public ResponseEntity<?> getSaleById(@Valid @RequestBody LoadRequest dto) throws ImsBusinessException {
        return ResponseEntity.ok(purchaseService.loadPurchase(dto));
    }

    @PostMapping("/download")
    public void downloadInvoice(@RequestBody LoadRequest dto, HttpServletResponse response) throws Exception {
        purchaseService.createPdf(dto, response);
    }
}
