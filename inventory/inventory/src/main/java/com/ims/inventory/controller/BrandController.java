package com.ims.inventory.controller;

import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.BrandRequest;
import com.ims.inventory.service.impl.BranchMasterService;
import com.ims.inventory.service.impl.BrandMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {
    @Autowired
    private BrandMasterService brandMasterService;

    @PostMapping("brandDropdown")
    public ResponseEntity<?> getBrandDropDown(
            @RequestBody BrandRequest brandRequest) throws Exception {
        return ResponseEntity.ok(brandMasterService.findAllBrand(brandRequest));
    }
}
