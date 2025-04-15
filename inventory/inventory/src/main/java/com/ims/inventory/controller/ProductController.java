package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.service.impl.CategoryMasterService;
import com.ims.inventory.service.impl.ProductMasterservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductMasterservice productMasterservice;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FindProductRequest findProductRequest) throws Exception {
        return ResponseEntity.ok(productMasterservice.findAllProductByIsActive(findProductRequest.isActive()));
    }

    @PostMapping("add")
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody ProductRequest productRequest) throws Exception {
        try {
            return ResponseEntity.ok(productMasterservice.addProduct(productRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("ProductController::addProduct:Exception occurred while product creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editProduct(
            @Valid @RequestBody ProductRequest productRequest) throws Exception {
        try {
            return ResponseEntity.ok(productMasterservice.editProduct(productRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("ProductController::addProduct:Exception occurred while product edition.", e);
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteProduct(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(productMasterservice.ProductDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("ProductController::addProduct:Exception occurred while product deletion.", e);
        }
    }
}
