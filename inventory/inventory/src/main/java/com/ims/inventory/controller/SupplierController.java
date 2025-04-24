package com.ims.inventory.controller;

import com.ims.inventory.domen.request.AutoCompleteRequest;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.SupplierRequest;
import com.ims.inventory.domen.response.SupplierResponse;
import com.ims.inventory.service.impl.SupplierServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/people/supplier/")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    @PostMapping("add")
    public ResponseEntity<?> addSupplier(
            @Valid @RequestBody SupplierRequest dto) throws Exception {
        try {
            return ResponseEntity.ok(supplierService.create(dto));
        } catch (BadCredentialsException e) {
            throw new Exception("SupplierController::addSupplier:Exception occurred while supplier creation.", e);
        }

    }

    @PostMapping("edit")
    public ResponseEntity<?> editSupplier(
            @Valid @RequestBody SupplierRequest dto) throws Exception {
        try {
            return ResponseEntity.ok(supplierService.editSupplier(dto));
        } catch (BadCredentialsException e) {
            throw new Exception("SupplierController::editSupplier:Exception occurred while Supplier edition.", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@PathVariable String id,
                                                   @RequestBody @Valid SupplierRequest dto) {
        return ResponseEntity.ok(supplierService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        supplierService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("dropdown")
    public ResponseEntity<?> getSearchCustomer(
            @RequestBody AutoCompleteRequest autoCompleteRequest) throws Exception {
        return ResponseEntity.ok(supplierService.findAllSupplierByNameIsActive(
                autoCompleteRequest.getSearch(), true));
    }

    @PostMapping("loadSupplier")
    public ResponseEntity<?> loadSupplier(
            @Valid @RequestBody LoadRequest loadRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(supplierService.loadSupplier(loadRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }
}
