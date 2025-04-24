package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.exception.ImportError;
import com.ims.inventory.service.impl.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/people/customer/")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @PostMapping("add")
    public ResponseEntity<?> addCustomer(
            @Valid @RequestBody CustomerRequest dto) throws Exception {
        try {
            return ResponseEntity.ok(customerService.create(dto));
        } catch (BadCredentialsException e) {
            throw new Exception("CustomerController::addRole:Exception occurred while role creation.", e);
        }

    }

    @PostMapping("edit")
    public ResponseEntity<?> editCutomer(
            @Valid @RequestBody CustomerRequest dto) throws Exception {
        try {
            return ResponseEntity.ok(customerService.editCustomer(dto));
        } catch (BadCredentialsException e) {
            throw new Exception("CustomerController::editCutomer:Exception occurred while customer edition.", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable String id,
                                                      @RequestBody @Valid CustomerRequest dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        customerService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public void exportCsv(HttpServletResponse response) throws IOException {
        customerService.exportToCsv(response);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        List<ImportError> errors = customerService.importFromCsv(file);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok("Import successful");
    }

    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=customers_template.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Customer Name,Email,Phone Number,Address Line1,City,State,Country,Pin Code");
        writer.println("John Doe,john@example.com,1234567890,123 Main St,New York,New York,USA,10001");
        writer.flush();
    }

    @PostMapping("dropdown")
    public ResponseEntity<?> getSearchCustomer(
            @RequestBody AutoCompleteRequest autoCompleteRequest) throws Exception {
        return ResponseEntity.ok(customerService.findAllCustomerByNameIsActive(
                autoCompleteRequest.getSearch(), true));
    }

    @PostMapping("loadCustomer")
    public ResponseEntity<?> loadCustomer(
            @Valid @RequestBody LoadRequest loadRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(customerService.loadCustomer(loadRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

}
