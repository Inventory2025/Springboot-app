package com.ims.inventory.controller;

import com.ims.inventory.domen.request.BrandRequest;
import com.ims.inventory.domen.request.UnitRequest;
import com.ims.inventory.service.impl.UnitMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/unit/")
public class UnitController {

    @Autowired
    UnitMasterService unitMasterService;

    @PostMapping("unitDropdown")
    public ResponseEntity<?> getUnitDropDown(
            @RequestBody UnitRequest unitRequest) throws Exception {
        return ResponseEntity.ok(unitMasterService.findAllUnit(unitRequest));
    }
}
