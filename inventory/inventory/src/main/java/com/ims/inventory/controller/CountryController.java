package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.service.RoleMasterService;
import com.ims.inventory.service.impl.CountryMasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    private CountryMasterService countryMasterService;

    @PostMapping("add")
    public ResponseEntity<?> addCountry(
            @Valid @RequestBody CountryRequest countryRequest) throws Exception {
        try {
            return ResponseEntity.ok(countryMasterService.addCountry(countryRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editCountry(
            @Valid @RequestBody CountryRequest countryRequest) throws Exception {
        try {
            return ResponseEntity.ok(countryMasterService.editCountry(countryRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCountry(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(countryMasterService.CountryDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role deletion.", e);
        }
    }

    @PostMapping("load")
    public ResponseEntity<?> loadCountry(
            @Valid @RequestBody LoadRequest loadRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(countryMasterService.load(loadRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @PostMapping("countryDropdown")
    public ResponseEntity<?> getCountryDropDown(
            @RequestBody CountryRequest countryRequest) throws Exception {
        return ResponseEntity.ok(countryMasterService.findAllCountry(countryRequest));
    }

}
